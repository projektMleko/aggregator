package pl.milk.DataManager.persistance.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import pl.milk.DataManager.persistance.model.Rating;

@Repository
public interface RatingRepository extends JpaRepository<Rating, Long> {
}
