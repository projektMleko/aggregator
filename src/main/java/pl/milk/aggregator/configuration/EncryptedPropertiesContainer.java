package pl.milk.aggregator.configuration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

// https://www.baeldung.com/spring-boot-jasypt

public class EncryptedPropertiesContainer {

    @Value("${accuweather.api.key}")
    String accuweatherApiKey;

    public String getAccuweatherApiKey() {
        return accuweatherApiKey;
    }
}
