package pl.atd.resilience;

import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.junit5.WireMockExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;

import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.serverError;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class ExternalApiTest {

    /**
     * start wire mock on port 9090
     */
    @RegisterExtension
    static WireMockExtension EXTERNAL_SERVICE = WireMockExtension.newInstance()
            .options(WireMockConfiguration.wireMockConfig().port(9090))
            .build();

    // another rest template used only for this test (do not confuse with rest template that calls external api)
    @Autowired
    private TestRestTemplate testRestTemplate;

    @Test
    public void testFoo() {
        // wire mock returns 500 error
        EXTERNAL_SERVICE.stubFor(get("/external-foo").willReturn(serverError()));

        // five times we get 500 error
        for (int i = 0; i < 5; i++) {
            ResponseEntity<String> response = testRestTemplate.getForEntity("/foo", String.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.INTERNAL_SERVER_ERROR);
        }
        // the next calls should return service unavailable (503) because of circuit breaker
        for (int i = 0; i < 5; i++) {
            ResponseEntity<String> response = testRestTemplate.getForEntity("/foo", String.class);
            assertThat(response.getStatusCode()).isEqualTo(HttpStatus.SERVICE_UNAVAILABLE);
        }
    }
}
