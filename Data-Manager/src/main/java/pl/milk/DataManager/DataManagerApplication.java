package pl.milk.DataManager;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import pl.milk.DataManager.kafka.AverageRatingStream;
import pl.milk.log.HeapInfoLogger;

@SpringBootApplication
public class DataManagerApplication  implements CommandLineRunner {
	private static final Logger logger = LoggerFactory.getLogger(DataManagerApplication.class);

	@Autowired
	AverageRatingStream averageRatingStream;

	public static void main(String[] args) {
		SpringApplication.run(DataManagerApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		logger.info("APPLICATION STARTED");
		System.out.println("APPLICATION STARTED");
		HeapInfoLogger.logHeapInfo();
		System.out.println("RUNNING STREAM");
		averageRatingStream.runStream();
	}
}
