package pl.milk.aggregator.persistance.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.milk.aggregator.persistance.model.Test;

import java.util.List;

@Repository
public interface TestRepository extends CrudRepository<Test, Long> {
    Test findById(long id);
    List<Test> findByTestContainerId(long testContainerId);
}
