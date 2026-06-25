package com.asj.rps.controller;

import com.asj.rps.dto.PlayRoundRequest;
import com.asj.rps.dto.RoundResponse;
import com.asj.rps.service.GameService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/rounds")
@RequiredArgsConstructor
public class GameRoundController {

    private final GameService gameService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public RoundResponse playRound(@Valid @RequestBody PlayRoundRequest request) {
        return gameService.playRound(request);
    }

    @GetMapping
    public List<RoundResponse> getHistory() {
        return gameService.getHistory();
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void resetGame() {
        gameService.resetGame();
    }
}
