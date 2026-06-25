package com.asj.rps.service;

import com.asj.rps.model.Move;
import com.asj.rps.model.RoundResult;
import org.springframework.stereotype.Component;

@Component
public class GameRules {

    public RoundResult evaluate(Move playerMove, Move computerMove) {
        if (playerMove == computerMove) {
            return RoundResult.DRAW;
        }

        return switch (playerMove) {
            case ROCK -> (computerMove == Move.SCISSORS) ? RoundResult.WIN : RoundResult.LOSS;
            case PAPER -> (computerMove == Move.ROCK) ? RoundResult.WIN : RoundResult.LOSS;
            case SCISSORS -> (computerMove == Move.PAPER) ? RoundResult.WIN : RoundResult.LOSS;
        };
    }
}
