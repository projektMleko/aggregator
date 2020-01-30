package pl.milk.DataManager.kafka;

import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.common.serialization.Deserializer;
import org.apache.kafka.common.serialization.Serde;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.common.serialization.Serializer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.StreamsConfig;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.errors.StreamsException;
import org.apache.kafka.streams.kstream.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.milk.DataManager.persistance.model.AverageRating;
import pl.milk.DataManager.persistance.model.Rating;
import pl.milk.DataManager.persistance.repository.RatingRepository;
import pl.milk.DataManager.serdes.JsonPOJODeserializer;
import pl.milk.DataManager.serdes.JsonPOJOSerializer;

import java.math.BigDecimal;
import java.util.*;
@Component
public class AverageRatingStream {
    private static final String TOPIC = "Ratings";
    @Autowired
    private RatingRepository ratingRepository;

    public void runStream() {
        System.out.println("START RUN STREAM");
        Properties props = new Properties();
        props.put(StreamsConfig.APPLICATION_ID_CONFIG, "streams-pipe");
        props.put(StreamsConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
        props.put(StreamsConfig.DEFAULT_KEY_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        props.put(StreamsConfig.DEFAULT_VALUE_SERDE_CLASS_CONFIG, Serdes.String().getClass());
        //props.put(StreamsConfig.DEFAULT_DESERIALIZATION_EXCEPTION_HANDLER_CLASS_CONFIG, LogAndContinueExceptionHandler.class);

        Serde<Rating> ratingSerde = getRatingSerde();

        final StreamsBuilder builder = new StreamsBuilder();
        builder.stream(TOPIC, Consumed.with(Serdes.String(), ratingSerde))
                .selectKey((k, v) -> v.getFkMovieId())
                .groupByKey(Grouped.with(Serdes.Long(), ratingSerde))
                .aggregate(Rating::new,
                        (k, v, agg) -> {
                            agg.setId(agg.getId()+1L);
                            agg.setFkMovieId(k);
                            agg.setFkUserId(new Date().getTime());
                            agg.setValue(agg.getValue().add(v.getValue()));
                            return agg; },
                        Materialized.with(Serdes.Long(), ratingSerde))
                .toStream()
                .mapValues(r -> new AverageRating()
                        .setValue(r.getValue().divide(BigDecimal.valueOf(r.getId())))
                        .setFkMovieId(r.getFkMovieId()))
                .to("RATING_AVG", Produced.with(Serdes.Long(), getAvgRatingSerde()));
        //FIXME db rating entity doesnt work because of some reasons
        //todo remember, do not use stream 2 times on same topic (Ratings) with same builder. It will throw an exception about registering same topic twice.
//      builder.stream(TOPIC, Consumed.with(Serdes.String(), ratingSerde))
//                .foreach((k, r) -> ratingRepository.save(r));

        final Topology topology = builder.build();
        System.out.println("Topology desc: " + topology.describe());
        final KafkaStreams streams = new KafkaStreams(topology, props);

        try {
            streams.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (StreamsException e) {
            e.printStackTrace();
        } finally {
        }
    }



    private Serde<Rating> getRatingSerde() {
        Map<String, Object> serdeProps = new HashMap<>();
        final Serializer<Rating> ratingJsonPOJOSerializer;
        ratingJsonPOJOSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", Rating.class);
        ratingJsonPOJOSerializer.configure(serdeProps, false);

        final Deserializer<Rating> ratingJsonPOJODeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", Rating.class);
        ratingJsonPOJODeserializer.configure(serdeProps, false);

        return Serdes.serdeFrom(ratingJsonPOJOSerializer, ratingJsonPOJODeserializer);
    }

    private Serde<AverageRating> getAvgRatingSerde() {
        Map<String, Object> serdeProps = new HashMap<>();
        final Serializer<AverageRating> ratingJsonPOJOSerializer;
        ratingJsonPOJOSerializer = new JsonPOJOSerializer<>();
        serdeProps.put("JsonPOJOClass", AverageRating.class);
        ratingJsonPOJOSerializer.configure(serdeProps, false);

        final Deserializer<AverageRating> ratingJsonPOJODeserializer = new JsonPOJODeserializer<>();
        serdeProps.put("JsonPOJOClass", AverageRating.class);
        ratingJsonPOJODeserializer.configure(serdeProps, false);

        return Serdes.serdeFrom(ratingJsonPOJOSerializer, ratingJsonPOJODeserializer);
    }

}
