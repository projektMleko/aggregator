package pl.milk.aggregator.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


public class BatchInsertSerivceImpl<T> implements BatchInsertSerivce<T> {

    private static final Logger logger = LoggerFactory.getLogger("WarnLogger");
    private final JpaRepository<T, Long> batchInsertRepository;

    public BatchInsertSerivceImpl(final JpaRepository<T, Long> batchInsertRepository) {
        this.batchInsertRepository = batchInsertRepository;
    }

    @Override
    public List<T> batchInsert(final List<T> group) {
        try {
            batchInsertRepository.saveAll(group);
        } catch (final DataIntegrityViolationException ex) {
            logger.warn("Error occurred during batch insert. Preparing individual insert.", ex);
            individualInsert(group);
        }
        return group;
    }

    private List<T> individualInsert(final List<T> group) {
        return group.stream()
                .map(this::insertItem)
                .flatMap(Optional::stream)
                .collect(Collectors.toList());
    }

    private Optional<T> insertItem(final T item) {
        try {
            return Optional.ofNullable(batchInsertRepository.save(item));
        } catch (final DataIntegrityViolationException ex) {
            logger.warn("Error occurred during individual insert. This row will be omitted.", ex);
            return Optional.empty();
        }
    }
}
