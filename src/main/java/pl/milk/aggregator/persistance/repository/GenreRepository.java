package pl.milk.aggregator.persistance.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.milk.aggregator.persistance.model.Genre;

import java.util.List;

@Repository
public interface GenreRepository extends CrudRepository<Genre, Long> {
    @Override
    Genre save(Genre genre);

    @Override
    List<Genre> findAll();
}
