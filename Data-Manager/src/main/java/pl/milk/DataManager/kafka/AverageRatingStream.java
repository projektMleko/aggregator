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
import org.apache.kafka.streams.errors.LogAndContinueExceptionHandler;
import org.apache.kafka.streams.errors.StreamsException;
import org.apache.kafka.streams.kstream.*;
import pl.milk.DataManager.persistance.Rating;
import pl.milk.DataManager.persistance.RatingTimestampWrapper;
import pl.milk.DataManager.serdes.JsonPOJODeserializer;
import pl.milk.DataManager.serdes.JsonPOJOSerializer;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.TimeUnit;

public class AverageRatingStream {
    private static final String TOPIC = "Ratings";

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

        //todo dodac by przy kazdym ratingu generowal komentarz. Najlepiej by byl to osobny kafkaStream, ktory to zrobi
//        builder.stream(TOPIC, Consumed.with(Serdes.String(), ratingSerde))
//                .groupBy((k, v) -> v.getFkMovieId(), Grouped.with(Serdes.Long(), ratingSerde))
//                .reduce((aggregator, v) -> {
//                    aggregator.setValue(aggregator.getValue().add(v.getValue()));
//                    Optional.ofNullable(aggregator.getId()).ifPresentOrElse(
//                            o -> aggregator.setId(o + 1L),
//                            () -> aggregator.setId(1L));
//                    return aggregator; })
//                .toStream(Named.as("Rating_aggregate"))
//                .mapValues(this::avgValue)
//                .to("Ratings_AVG", Produced.with(Serdes.Long(), ratingSerde));
                //.foreach((k, v) -> System.out.println("Event from Rating: " + v));

        //todo najlepsze na te chwile
//        builder.stream(TOPIC, Consumed.with(Serdes.String(), ratingSerde))
//                .selectKey((k, v) -> v.getFkMovieId())  //to powoduje repartioning (str. 113)
//                .groupBy((k, v) -> v.getFkMovieId(), Grouped.with(Serdes.Long(), ratingSerde))
//                .aggregate(() -> new Rating(),
//                            (k, v, agg) -> {
//                                agg.setId(agg.getId()+1L);
//                                agg.setFkMovieId(k);
//                                agg.setValue(agg.getValue().add(v.getValue()));
//                                return agg; },
//                            Materialized.with(Serdes.Long(), ratingSerde))
//
//                // stateful operation, czyli zawiera acumulator
//                .mapValues(this::avgValue, Materialized.with(Serdes.Long(), ratingSerde))
//                .toStream()
//                .to("Ratings_AVG", Produced.with(Serdes.Long(), ratingSerde));

        //todo zrrobic by ta topologia:
        // a) zracala avg_rating to topicu
        // b) zapisywala sie do bazy w tradycyjny sposob
        // b) 1) do tego dodac obraz bazy oraclowej :P
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
                .print(Printed.toSysOut());

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
           // streams.close();
         //   System.out.println("END RUN STREAM");
        }
    }

    private Rating avgValue(Rating v) {
        //System.out.println("Mapuje: " + v);
        v.setValue(v.getValue().divide(BigDecimal.valueOf(v.getId())));
        return v;
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

}
