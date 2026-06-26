package com.asj.rps.controller;

import com.asj.rps.dto.StatisticsResponse;
import com.asj.rps.service.GameService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(StatisticsController.class)
class StatisticsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private GameService gameService;

    @Test
    void getStatistics_Returns200() throws Exception {
        StatisticsResponse response = StatisticsResponse.builder()
                .totalRounds(10)
                .wins(5)
                .losses(3)
                .draws(2)
                .winRate(50.0)
                .build();

        when(gameService.getStatistics()).thenReturn(response);

        mockMvc.perform(get("/api/statistics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.totalRounds").value(10))
                .andExpect(jsonPath("$.winRate").value(50.0));
    }
}
