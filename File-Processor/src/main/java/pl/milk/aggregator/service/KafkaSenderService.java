package pl.milk.aggregator.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import pl.milk.aggregator.kafka.AggregatorKafkaProducer;
import pl.milk.aggregator.kafka.Producer;

public class KafkaSenderService<T> {

    private ObjectMapper objectMapper;
    private AggregatorKafkaProducer<T> aggregatorKafkaProducer;
    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);

    public KafkaSenderService(ObjectMapper objectMapper, AggregatorKafkaProducer<T> aggregatorKafkaProducer) {
        this.objectMapper = objectMapper;
        this.aggregatorKafkaProducer = aggregatorKafkaProducer;
    }

    public void sendModel(T model) {

    Try.of(() -> objectMapper.writeValueAsString(model))
            .flatMapTry(m -> Try.run(() -> aggregatorKafkaProducer.produceEvent(m)))
            .onFailure(e -> LOGGER.error("Exception occurred during kafka event sending.", e));
    }
}
