package pl.milk.aggregator.service;

public interface FileProcessingService<T> {
    void processFile(String filePath);
}
