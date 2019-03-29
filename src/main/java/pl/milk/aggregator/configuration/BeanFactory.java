package pl.milk.aggregator.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanFactory {

    @Bean
    public EncryptedPropertiesContainer getEncryptedPropertiesContainer() {
        System.setProperty("jasypt.encryptor.password", System.getenv("AGGREGATOR_SECRET_KEY"));
        return new EncryptedPropertiesContainer();
    }
}
