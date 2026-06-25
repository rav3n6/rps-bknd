package com.asj.rps.repository;

import com.asj.rps.entity.GameRound;
import com.asj.rps.model.Move;
import com.asj.rps.model.RoundResult;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
@ActiveProfiles("test")
class GameRoundRepositoryTest {

    @Autowired
    private GameRoundRepository repository;

    @Test
    void findAllByOrderByPlayedAtDesc_ShouldReturnNewestFirst() {
        GameRound round1 = new GameRound();
        round1.setPlayedAt(Instant.parse("2026-06-23T16:30:00Z"));
        repository.save(round1);

        GameRound round2 = new GameRound();
        round2.setPlayedAt(Instant.parse("2026-06-23T16:31:00Z"));
        repository.save(round2);

        List<GameRound> rounds = repository.findAllByOrderByPlayedAtDesc();

        assertEquals(2, rounds.size());
        assertEquals(round2.getId(), rounds.get(0).getId());
    }

    @Test
    void countByResult_ShouldReturnCorrectCount() {
        GameRound round1 = new GameRound();
        round1.setResult(RoundResult.WIN);
        repository.save(round1);

        GameRound round2 = new GameRound();
        round2.setResult(RoundResult.WIN);
        repository.save(round2);

        GameRound round3 = new GameRound();
        round3.setResult(RoundResult.LOSS);
        repository.save(round3);

        long wins = repository.countByResult(RoundResult.WIN);
        long losses = repository.countByResult(RoundResult.LOSS);

        assertEquals(2L, wins);
        assertEquals(1L, losses);
    }
}
