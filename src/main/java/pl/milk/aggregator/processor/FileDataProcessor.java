package pl.milk.aggregator.processor;

public interface FileDataProcessor<T> {
    T processFileLine(String fileLine);
}
