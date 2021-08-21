package bernardino.manning.tracing;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class EShopControllerIntTest {

    @Autowired
    private MockMvc mockMvc;

    public void checkoutIsMadeWithSuccess() throws Exception {
        mockMvc.perform(post("/checkout"))
                .andExpect(status().isOk());
    }
}