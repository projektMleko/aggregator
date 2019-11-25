package pl.milk.aggregator.persistance.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import pl.milk.aggregator.persistance.model.TestContainer;

@Repository
public interface TestContainerRepository extends CrudRepository<TestContainer, Long> {
    TestContainer findByTests(long testId);
    TestContainer findById(long id);

}
