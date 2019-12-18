package pl.milk.aggregator.configuration;

import akka.actor.ActorSystem;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.milk.aggregator.kafka.AggregatorKafkaProducer;
import pl.milk.aggregator.kafka.MovieProducer;
import pl.milk.aggregator.kafka.Producer;
import pl.milk.aggregator.kafka.RatingProducer;
import pl.milk.aggregator.persistance.model.Movie;
import pl.milk.aggregator.persistance.model.Rating;
import pl.milk.aggregator.persistance.repository.MovieRepository;
import pl.milk.aggregator.persistance.repository.RatingRepository;
import pl.milk.aggregator.processor.MovieProcessor;
import pl.milk.aggregator.processor.RatingProcessor;
import pl.milk.aggregator.service.*;

@Configuration
public class BeanFactory {

    @Bean
    public ObjectMapper getObjectToJSONMapper() {
        return new ObjectMapper();
    }

    @Bean
    public ActorSystem getFileProcessingActorSystem() {
        return ActorSystem.create("file-processing");
    }

    @Bean
    public EncryptedPropertiesContainer getEncryptedPropertiesContainer() {
        System.setProperty("jasypt.encryptor.password", System.getenv("AGGREGATOR_SECRET_KEY"));
        return new EncryptedPropertiesContainer();
    }

    @Bean
    public BatchInsertSerivce<Rating> getRatingBatchInsertService(final RatingRepository movieRepository) {
        return new BatchInsertSerivceImpl<>(movieRepository);
    }

    @Bean
    public FileProcessingService<Rating> getFileRatingProcessingService(
            final RatingProcessor movieProcessor,
            final KafkaSenderService<Rating> kafkaSenderService) {
        return new FileProcessingServiceImpl<>(movieProcessor, kafkaSenderService);
    }


    @Bean
    public BatchInsertSerivce<Movie> getMovieBatchInsertService(final MovieRepository movieRepository) {
        return new BatchInsertSerivceImpl<>(movieRepository);
    }

    @Bean
    public FileProcessingService<Movie> getFileMovieProcessingService(
                                                                      final MovieProcessor movieProcessor,
                                                                      final KafkaSenderService<Movie> kafkaSenderService) {
        return new FileProcessingServiceImpl<>(movieProcessor, kafkaSenderService);
    }

    @Bean
    public KafkaSenderService<Movie> getMovieKafkaSenderSerivce(ObjectMapper objectMapper, MovieProducer movieProducer) {
        return new KafkaSenderService<>(objectMapper, movieProducer);
    }

    @Bean
    public KafkaSenderService<Rating> getRatingKafkaSenderSerivce(ObjectMapper objectMapper, RatingProducer ratingProducer) {
        return new KafkaSenderService<>(objectMapper, ratingProducer);
    }
}
