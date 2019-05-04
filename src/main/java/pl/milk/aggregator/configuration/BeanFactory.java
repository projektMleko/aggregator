package pl.milk.aggregator.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import pl.milk.aggregator.persistance.model.Movie;
import pl.milk.aggregator.persistance.repository.MovieRepository;
import pl.milk.aggregator.processor.MovieProcessor;
import pl.milk.aggregator.service.FileProcessingService;
import pl.milk.aggregator.service.FileProcessingServiceImpl;

@Configuration
public class BeanFactory {

    @Bean
    public EncryptedPropertiesContainer getEncryptedPropertiesContainer() {
        System.setProperty("jasypt.encryptor.password", System.getenv("AGGREGATOR_SECRET_KEY"));
        return new EncryptedPropertiesContainer();
    }

//    @Bean
//    public FileProcessingService<Rating> getFileRatingProcessingService(final RatingProcessor ratingProcessor,
//                                                                        final RatingRepository ratingRepository) {
//        return new FileProcessingServiceImpl<>(ratingProcessor, ratingRepository);
//    }

    @Bean
    public FileProcessingService<Movie> getFileMovieProcessingService(final MovieProcessor movieProcessor,
                                                                      final MovieRepository movieRepository) {
        return new FileProcessingServiceImpl<>(movieProcessor, movieRepository);
    }
}
