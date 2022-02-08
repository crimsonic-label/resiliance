package pl.atd.resilience;

import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalService {

    private final RestTemplate restTemplate;
    private final CircuitBreakerRegistry circuitBreakerRegistry;

    /**
     * circuit breaker decorates calls to external API using RestTemplate
     *
     * @return text returned by external API
     */
    public String callExternalFoo() {
        log.info("calling external foo...");
        return circuitBreakerRegistry
                .circuitBreaker("externalServiceFoo", "externalServiceFoo")
                .executeSupplier(() -> restTemplate.getForObject("/external-foo", String.class));
    }
}
