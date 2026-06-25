package com.asj.rps.dto;

import com.asj.rps.entity.GameRound;
import com.asj.rps.model.Move;
import com.asj.rps.model.RoundResult;
import lombok.Data;

import java.time.Instant;

@Data
public class RoundResponse {
    private Long id;
    private Move playerMove;
    private Move computerMove;
    private RoundResult result;
    private Instant playedAt;

    public static RoundResponse fromEntity(GameRound round) {
        RoundResponse response = new RoundResponse();
        response.setId(round.getId());
        response.setPlayerMove(round.getPlayerMove());
        response.setComputerMove(round.getComputerMove());
        response.setResult(round.getResult());
        response.setPlayedAt(round.getPlayedAt());
        return response;
    }
}
