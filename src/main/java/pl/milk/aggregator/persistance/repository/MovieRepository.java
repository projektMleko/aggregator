package pl.milk.aggregator.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.milk.aggregator.persistance.model.Movie;

public interface MovieRepository extends JpaRepository<Movie, Long> {
}
