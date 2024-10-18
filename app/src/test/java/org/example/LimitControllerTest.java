package org.example;

import org.example.entity.Limit;
import org.example.entity.RecurringFrequency;
import org.example.service.LimitService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LimitControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private LimitService limitService;

    @Test
    public void getUsersLimitsTest() throws Exception {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        List<Limit> limits = new ArrayList<>();
        Limit limit = new Limit();
        limit.setMaxExpenseLimit(BigDecimal.valueOf(10000.0));
        limit.setFrequency(RecurringFrequency.MONTHLY);
        limits.add(limit);

        when(limitService.getUsersLimits(userId)).thenReturn(limits);

        mockMvc.perform(get("/api/v1/limits/all")
                .param("userId", userId.toString()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].maxExpenseLimit").value(BigDecimal.valueOf(10000.0)))
                .andExpect(jsonPath("$[0].frequency").value("MONTHLY"));
    }

    @Test
    public void addLimitTest() throws Exception {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000000");

        mockMvc.perform(post("/api/v1/limits/add")
                        .param("userId", userId.toString())
                        .param("maxExpense", "10000.0")
                        .param("frequency", "MONTHLY"))
                .andExpect(status().isOk())
                .andExpect(content().string("Limit added"));
    }

    @Test
    public void deleteLimitTest() throws Exception {
        UUID userId = UUID.fromString("00000000-0000-0000-0000-000000000000");
        Limit limit = new Limit();
        limit.setMaxExpenseLimit(BigDecimal.valueOf(10000.0));
        limit.setFrequency(RecurringFrequency.MONTHLY);
        limitService.addLimit(userId, BigDecimal.valueOf(10000.0), RecurringFrequency.MONTHLY);
        limitService.deleteLimit(userId, RecurringFrequency.MONTHLY);

        mockMvc.perform(delete("/api/v1/limits/delete")
                        .param("userId", userId.toString())
                        .param("frequency", "MONTHLY"))
                .andExpect(status().isOk());
    }
}
