package pl.milk.aggregator.search.configuration;


import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class BeanFactory {

    @Bean
    HazelcastInstance getHazelcastInstance() {
        return Hazelcast.newHazelcastInstance();
    }
}
