package com.skillbox.fibonacci.adapter.web;

import com.skillbox.fibonacci.PostgresTestContainerInitializer;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Import(PostgresTestContainerInitializer.class)
class FibonacciControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldReturnFibonacciNumberWithStatus200WhenIndexIsValid() throws Exception {
        mockMvc.perform(get("/fibonacci/9"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.index").value(9))
                .andExpect(jsonPath("$.value").value(34));
    }

    @Test
    void shouldReturnBadRequestWhenIndexIsLessThanOne() throws Exception {
        mockMvc.perform(get("/fibonacci/-1"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("warning"));
    }

    @Test
    void shouldReturnBadRequestWhenIndexIsZero() throws Exception {
        mockMvc.perform(get("/fibonacci/0"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value("warning"));
    }

    @Test
    void shouldReturnCorrectValueOnFirstRequestForIndex47() throws Exception {
        mockMvc.perform(get("/fibonacci/47"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.index").value(47))
                .andExpect(jsonPath("$.value").value(2971215073L));
    }

    @Test
    void shouldReturnSameValueOnRepeatedRequestForIndex47() throws Exception {
        mockMvc.perform(get("/fibonacci/47"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.value").value(2971215073L));

        mockMvc.perform(get("/fibonacci/47"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.index").value(47))
                .andExpect(jsonPath("$.value").value(2971215073L));
    }
}
