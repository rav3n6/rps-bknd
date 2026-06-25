package com.asj.rps.service;

import com.asj.rps.dto.PlayRoundRequest;
import com.asj.rps.dto.RoundResponse;
import com.asj.rps.dto.StatisticsResponse;
import com.asj.rps.entity.GameRound;
import com.asj.rps.model.Move;
import com.asj.rps.model.RoundResult;
import com.asj.rps.repository.GameRoundRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class GameService {

    private final GameRoundRepository repository;
    private final ComputerMoveGenerator moveGenerator;
    private final GameRules gameRules;

    @Transactional
    public RoundResponse playRound(PlayRoundRequest request) {
        Move computerMove = moveGenerator.generateMove();
        RoundResult result = gameRules.evaluate(request.getPlayerMove(), computerMove);

        GameRound round = new GameRound();
        round.setPlayerMove(request.getPlayerMove());
        round.setComputerMove(computerMove);
        round.setResult(result);
        round.setPlayedAt(Instant.now());

        GameRound savedRound = repository.save(round);
        return RoundResponse.fromEntity(savedRound);
    }

    @Transactional(readOnly = true)
    public List<RoundResponse> getHistory() {
        return repository.findAllByOrderByPlayedAtDesc().stream()
                .map(RoundResponse::fromEntity)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public StatisticsResponse getStatistics() {
        long wins = repository.countByResult(RoundResult.WIN);
        long losses = repository.countByResult(RoundResult.LOSS);
        long draws = repository.countByResult(RoundResult.DRAW);

        long totalRounds = wins + losses + draws;

        double winRate = 0.0;
        if (totalRounds > 0) {
            double rawWinRate = (double) wins / totalRounds * 100.0;
            winRate = BigDecimal.valueOf(rawWinRate)
                    .setScale(2, RoundingMode.HALF_UP)
                    .doubleValue();
        }

        return StatisticsResponse.builder()
                .totalRounds(totalRounds)
                .wins(wins)
                .losses(losses)
                .draws(draws)
                .winRate(winRate)
                .build();
    }

    @Transactional
    public void resetGame() {
        repository.deleteAll();
    }
}
