package pl.milk.aggregator.kafka;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import pl.milk.aggregator.persistance.model.Rating;

import javax.annotation.PostConstruct;

@Component
public class RatingProducer implements AggregatorKafkaProducer<Rating> {
    @Value("${file.processing.kafka.cluster.name}")
    private String kafkaClusterName;
    @Value("${file.processing.kafka.cluster.localization}")
    private String kafkaClusterLocalization;
    private Producer producer;

    @PostConstruct
    void createProducer() {
        this.producer = new Producer("Ratings", kafkaClusterName, kafkaClusterLocalization);
    }
    @Override
    public void produceEvent(String jsonEvent) {
        producer.sendEvent(jsonEvent);
    }
}
