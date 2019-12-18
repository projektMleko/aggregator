package pl.milk.aggregator.kafka;

public interface AggregatorKafkaProducer<T> {
    void produceEvent(String jsonEvent);
}
