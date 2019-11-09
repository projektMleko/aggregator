package pl.milk.aggregator.service;

import java.util.List;


public interface BatchInsertSerivce<T> {
    List<T> batchInsert(final List<T> group);
}
