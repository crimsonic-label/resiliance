package pl.atd.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreakerConfig;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.common.circuitbreaker.configuration.CircuitBreakerConfigCustomizer;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;
import java.util.Map;

@Configuration
public class ExternalApiConfig {

    /**
     * Configure RestTemplate bean with root URI
     *
     * @return rest template
     */
    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplateBuilder().rootUri("http://localhost:9090").build();
    }

    /**
     * sliding window with a size of 10, the evaluation will happen after the first 5 calls are done and
     * we’ll wait 5 seconds in OPEN state and the failure threshold is set to 50% meaning that out of 10 requests,
     * if 5 fails, the CircuitBreaker will open
     *
     * @return circuit breaker registry
     */
    @Bean
    public CircuitBreakerRegistry circuitBreakerRegistry() {
        CircuitBreakerConfig externalServiceFooConfig = CircuitBreakerConfig.custom()
                .slidingWindowSize(10)
                .slidingWindowType(CircuitBreakerConfig.SlidingWindowType.COUNT_BASED)
                .waitDurationInOpenState(Duration.ofSeconds(5))
                .minimumNumberOfCalls(5)
                .failureRateThreshold(50.0f)
                .build();

        return CircuitBreakerRegistry.of(Map.of("externalServiceFoo", externalServiceFooConfig));
    }
}
