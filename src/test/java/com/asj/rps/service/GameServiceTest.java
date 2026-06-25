package com.asj.rps.service;

import com.asj.rps.dto.PlayRoundRequest;
import com.asj.rps.dto.RoundResponse;
import com.asj.rps.dto.StatisticsResponse;
import com.asj.rps.entity.GameRound;
import com.asj.rps.model.Move;
import com.asj.rps.model.RoundResult;
import com.asj.rps.repository.GameRoundRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class GameServiceTest {

    @Mock
    private GameRoundRepository repository;

    @Mock
    private ComputerMoveGenerator moveGenerator;

    @Mock
    private GameRules gameRules;

    @InjectMocks
    private GameService gameService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void playRound_ShouldSaveAndReturnRound() {
        PlayRoundRequest request = new PlayRoundRequest();
        request.setPlayerMove(Move.ROCK);

        when(moveGenerator.generateMove()).thenReturn(Move.SCISSORS);
        when(gameRules.evaluate(Move.ROCK, Move.SCISSORS)).thenReturn(RoundResult.WIN);

        GameRound savedRound = new GameRound();
        savedRound.setId(1L);
        savedRound.setPlayerMove(Move.ROCK);
        savedRound.setComputerMove(Move.SCISSORS);
        savedRound.setResult(RoundResult.WIN);
        savedRound.setPlayedAt(Instant.now());

        when(repository.save(any(GameRound.class))).thenReturn(savedRound);

        RoundResponse response = gameService.playRound(request);

        assertNotNull(response);
        assertEquals(1L, response.getId());
        assertEquals(Move.ROCK, response.getPlayerMove());
        assertEquals(Move.SCISSORS, response.getComputerMove());
        assertEquals(RoundResult.WIN, response.getResult());

        verify(repository).save(any(GameRound.class));
    }

    @Test
    void getHistory_ShouldReturnRounds() {
        GameRound round = new GameRound();
        round.setId(1L);
        when(repository.findAllByOrderByPlayedAtDesc()).thenReturn(List.of(round));

        List<RoundResponse> history = gameService.getHistory();

        assertEquals(1, history.size());
        assertEquals(1L, history.get(0).getId());
    }

    @Test
    void getStatistics_ShouldCalculateCorrectly() {
        when(repository.countByResult(RoundResult.WIN)).thenReturn(5L);
        when(repository.countByResult(RoundResult.LOSS)).thenReturn(3L);
        when(repository.countByResult(RoundResult.DRAW)).thenReturn(2L);

        StatisticsResponse stats = gameService.getStatistics();

        assertEquals(10L, stats.getTotalRounds());
        assertEquals(5L, stats.getWins());
        assertEquals(3L, stats.getLosses());
        assertEquals(2L, stats.getDraws());
        assertEquals(50.0, stats.getWinRate());
    }

    @Test
    void getStatistics_WhenNoRounds_ShouldReturnZeroWinRate() {
        when(repository.countByResult(any())).thenReturn(0L);

        StatisticsResponse stats = gameService.getStatistics();

        assertEquals(0L, stats.getTotalRounds());
        assertEquals(0.0, stats.getWinRate());
    }

    @Test
    void resetGame_ShouldDeleteAll() {
        gameService.resetGame();
        verify(repository).deleteAll();
    }
}
