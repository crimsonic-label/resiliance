package pl.atd.resilience;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller to call service with external api
 */
@RestController
@RequiredArgsConstructor
@Slf4j
public class FooController {

    @Autowired
    private ExternalService externalService;

    @GetMapping("/foo")
    public String foo() {
        log.info("Foo endpoint called");
        return externalService.callExternalFoo();
    }
}
