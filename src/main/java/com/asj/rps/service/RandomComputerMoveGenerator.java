package com.asj.rps.service;

import com.asj.rps.model.Move;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class RandomComputerMoveGenerator implements ComputerMoveGenerator {

    private final Random random = new Random();

    @Override
    public Move generateMove() {
        Move[] moves = Move.values();
        return moves[random.nextInt(moves.length)];
    }
}
