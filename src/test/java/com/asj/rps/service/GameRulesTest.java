package com.asj.rps.service;

import com.asj.rps.model.Move;
import com.asj.rps.model.RoundResult;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GameRulesTest {

    private final GameRules gameRules = new GameRules();

    @ParameterizedTest
    @CsvSource({
            "ROCK, ROCK, DRAW",
            "ROCK, PAPER, LOSS",
            "ROCK, SCISSORS, WIN",
            "PAPER, ROCK, WIN",
            "PAPER, PAPER, DRAW",
            "PAPER, SCISSORS, LOSS",
            "SCISSORS, ROCK, LOSS",
            "SCISSORS, PAPER, WIN",
            "SCISSORS, SCISSORS, DRAW"
    })
    void testEvaluate(Move playerMove, Move computerMove, RoundResult expectedResult) {
        RoundResult result = gameRules.evaluate(playerMove, computerMove);
        assertEquals(expectedResult, result);
    }
}
