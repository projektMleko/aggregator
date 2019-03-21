package pl.milk.aggregator;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.annotation.PropertySources;
import org.springframework.scheduling.annotation.EnableScheduling;
import pl.milk.aggregator.log.ActuatorMetricLogger;

@SpringBootApplication
@EnableScheduling
@PropertySources({
		@PropertySource("/application.properties"),
		@PropertySource("/dbconfig.properties")
})
public class AggregatorApplication {
	private static final Logger logger = LoggerFactory.getLogger(AggregatorApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(AggregatorApplication.class, args);
		logger.info("APPLICATION STARTED");

	}

}
