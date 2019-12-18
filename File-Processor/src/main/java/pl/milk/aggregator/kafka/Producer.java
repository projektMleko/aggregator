package pl.milk.aggregator.kafka;

import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.Properties;



public class Producer {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);
    private  String topicName;
    private String kafkaClusterName;
    private String kafkaClusterLocalization;
    private KafkaProducer<String, String> kafkaProducer = null;
    private final String STRING_SERIALIZER_CLASS = "org.apache.kafka.common.serialization.StringSerializer";
    private final String STRING_DESERIALIZER_CLASS = "org.apache.kafka.common.serialization.StringSerializer";
    private final String DONT_WAIT_FOR_ACK = "0";


    public Producer(String topic, String kafkaClusterName, String kafkaClusterLocalization) {
        this.topicName = topic;
        this.kafkaClusterName = kafkaClusterName;
        this.kafkaClusterLocalization = kafkaClusterLocalization;
        this.kafkaProducer = new KafkaProducer<>(getKafkaProducerProperties());
    }

    private Properties getKafkaProducerProperties() {
        Properties kafkaProps = new Properties();
        String defaultClusterValue = kafkaClusterLocalization;
        String kafkaCluster = System.getProperty(kafkaClusterName, defaultClusterValue);
        LOGGER.info( "Kafka cluster {0}", kafkaCluster);
        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaCluster);
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, STRING_SERIALIZER_CLASS);
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, STRING_DESERIALIZER_CLASS);
        kafkaProps.put(ProducerConfig.ACKS_CONFIG, DONT_WAIT_FOR_ACK);
        return kafkaProps;
    }

    public void sendEvent(String event) {
        ProducerRecord<String, String> record = null;
        record = new ProducerRecord<>(topicName, null, event);
        kafkaProducer.send(record, this::eventSendCallback);
    }

    private void eventSendCallback(RecordMetadata recordMetadata, Exception exception) {
        if(Objects.nonNull(exception)){
            LOGGER.error("An error occurred during sending message to kafka topic: {}. ", topicName, exception);
        }
    }
}
