package pl.milk.aggregator;

import com.ulisesbocchio.jasyptspringboot.annotation.EncryptablePropertySource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.env.Environment;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.milk.aggregator.kafka.Producer;
import pl.milk.aggregator.log.HeapInfoLogger;

@SpringBootApplication
@EnableScheduling
@EncryptablePropertySource("encrypted.properties")
public class AggregatorApplication implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(AggregatorApplication.class);

    @Autowired
    Environment env;

    public static void main(final String[] args) {
        SpringApplication.run(AggregatorApplication.class, args);
    }

    @Override
    public void run(final String... args) throws Exception {
        logger.info("APPLICATION STARTED");
        System.out.println("APPLICATION STARTED");
        logger.info("Running in profile: " + env.getActiveProfiles()[0]);
        System.out.println("Running in profile: " + env.getActiveProfiles()[0]);
        HeapInfoLogger.logHeapInfo();
        System.out.println("KAFKA PRODUCER TEST START");
        Producer kafkaProducer = new Producer();
        kafkaProducer.run();
        System.out.println("KAFKA PRODUCER TEST END");
    }
}
