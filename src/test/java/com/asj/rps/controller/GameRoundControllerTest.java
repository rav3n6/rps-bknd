package com.asj.rps.controller;

import com.asj.rps.dto.PlayRoundRequest;
import com.asj.rps.dto.RoundResponse;
import com.asj.rps.model.Move;
import com.asj.rps.model.RoundResult;
import com.asj.rps.service.GameService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.time.Instant;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(GameRoundController.class)
class GameRoundControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private GameService gameService;

    @Test
    void playRound_ValidMove_Returns201() throws Exception {
        PlayRoundRequest request = new PlayRoundRequest();
        request.setPlayerMove(Move.ROCK);

        RoundResponse response = new RoundResponse();
        response.setId(1L);
        response.setPlayerMove(Move.ROCK);
        response.setComputerMove(Move.SCISSORS);
        response.setResult(RoundResult.WIN);
        response.setPlayedAt(Instant.now());

        when(gameService.playRound(any())).thenReturn(response);

        mockMvc.perform(post("/api/rounds")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.result").value("WIN"));
    }

    @Test
    void playRound_MissingMove_Returns400() throws Exception {
        mockMvc.perform(post("/api/rounds")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("playerMove must be provided"));
    }

    @Test
    void playRound_InvalidEnum_Returns400() throws Exception {
        mockMvc.perform(post("/api/rounds")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"playerMove\":\"INVALID\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Invalid move. Allowed values are ROCK, PAPER and SCISSORS"));
    }

    @Test
    void getHistory_Returns200() throws Exception {
        when(gameService.getHistory()).thenReturn(List.of(new RoundResponse()));

        mockMvc.perform(get("/api/rounds"))
                .andExpect(status().isOk());
    }

    @Test
    void resetGame_Returns204() throws Exception {
        mockMvc.perform(delete("/api/rounds"))
                .andExpect(status().isNoContent());

        verify(gameService).resetGame();
    }
}
