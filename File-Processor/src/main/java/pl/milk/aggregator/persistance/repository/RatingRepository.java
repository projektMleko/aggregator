package pl.milk.aggregator.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.milk.aggregator.persistance.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
}
