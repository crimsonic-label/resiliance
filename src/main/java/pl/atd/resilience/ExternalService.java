package pl.atd.resilience;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@RequiredArgsConstructor
@Slf4j
public class ExternalService {

    private final RestTemplate restTemplate;

    public String callExternalFoo() {
        log.info("calling external foo...");
        return restTemplate.getForObject("/external-foo", String.class);
    }
}
