package pl.milk.aggregator.kafka;

import org.apache.kafka.clients.producer.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Properties;
import java.util.Random;




/**
 * Runs in a dedicated thread. Contains core logic for producing event.
 */
public class Producer implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(Producer.class);
    private static final String TOPIC_NAME = "Movies";
    private KafkaProducer<String, String> kafkaProducer = null;
    private final String KAFKA_CLUSTER_ENV_VAR_NAME = "KAFKA_CLUSTER";

    public Producer() {
        LOGGER.info( "Kafka Producer running in thread {0}", Thread.currentThread().getName());
        Properties kafkaProps = new Properties();

        String defaultClusterValue = "localhost:32783";
        String kafkaCluster = System.getProperty(KAFKA_CLUSTER_ENV_VAR_NAME, defaultClusterValue);
        LOGGER.info( "Kafka cluster {0}", kafkaCluster);

        kafkaProps.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, kafkaCluster);
        kafkaProps.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        kafkaProps.put(ProducerConfig.ACKS_CONFIG, "0");

        this.kafkaProducer = new KafkaProducer<>(kafkaProps);

    }

    @Override
    public void run() {
        try {
            produce();
        } catch (Exception e) {
            LOGGER.error(e.getMessage(), e);
        }
    }

    /**
     * produce messages
     *
     * @throws Exception
     */
    private void produce() throws Exception {
        ProducerRecord<String, String> record = null;

        try {
            Random rnd = new Random();
            while (true) {

                for (int i = 1; i <= 10; i++) {
                    String key = "machine-" + i;
                    String value = String.valueOf(rnd.nextInt(20));
                    record = new ProducerRecord<>(TOPIC_NAME, key, value);

                    kafkaProducer.send(record, new Callback() {
                        @Override
                        public void onCompletion(RecordMetadata rm, Exception excptn) {
                            if (excptn != null) {
                                LOGGER.warn("Error sending message with key {0}\n{1}", new Object[]{key, excptn.getMessage()});
                            } else {
                                LOGGER.info("Partition for key-value {0}::{1} is {2}", new Object[]{key, value, rm.partition()});
                            }

                        }
                    });
                    /**
                     * wait before sending next message. this has been done on
                     * purpose
                     */
                    Thread.sleep(1000);
                }

            }
        } catch (Exception e) {
            LOGGER.error("Producer thread was interrupted");
        } finally {
            kafkaProducer.close();

            LOGGER.info( "Producer closed");
        }

    }

}
