package pl.atd.resilience;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(FooController.class)
class FooControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ExternalService externalService;

    @Test
    void shouldCallFoo() throws Exception {
        when(externalService.callExternalFoo()).thenReturn("Fooooo!");
        mockMvc.perform(get("/foo"))
                .andExpect(status().isOk())
                .andExpect(content().string("Fooooo!"));
    }
}