package pl.milk.aggregator.configuration;

import akka.actor.ActorSystem;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.milk.aggregator.persistance.model.Movie;
import pl.milk.aggregator.persistance.model.Rating;
import pl.milk.aggregator.persistance.repository.MovieRepository;
import pl.milk.aggregator.persistance.repository.RatingRepository;
import pl.milk.aggregator.processor.MovieProcessor;
import pl.milk.aggregator.processor.RatingProcessor;
import pl.milk.aggregator.service.BatchInsertSerivce;
import pl.milk.aggregator.service.BatchInsertSerivceImpl;
import pl.milk.aggregator.service.FileProcessingService;
import pl.milk.aggregator.service.FileProcessingServiceImpl;

@Configuration
public class BeanFactory {

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
    public FileProcessingService<Rating> getFileRatingProcessingService(final ActorSystem actorSystem,
                                                                        final RatingProcessor movieProcessor,
                                                                        final BatchInsertSerivce<Rating> batchInsertSerivce) {
        return new FileProcessingServiceImpl<>(actorSystem, movieProcessor, batchInsertSerivce);
    }


    @Bean
    public BatchInsertSerivce<Movie> getMovieBatchInsertService(final MovieRepository movieRepository) {
        return new BatchInsertSerivceImpl<>(movieRepository);
    }

    @Bean
    public FileProcessingService<Movie> getFileMovieProcessingService(final ActorSystem actorSystem,
                                                                      final MovieProcessor movieProcessor,
                                                                      final BatchInsertSerivce<Movie> batchInsertSerivce) {
        return new FileProcessingServiceImpl<>(actorSystem, movieProcessor, batchInsertSerivce);
    }
}
