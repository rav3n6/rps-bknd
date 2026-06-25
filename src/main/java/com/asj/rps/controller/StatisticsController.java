package com.asj.rps.controller;

import com.asj.rps.dto.StatisticsResponse;
import com.asj.rps.service.GameService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/statistics")
@RequiredArgsConstructor
public class StatisticsController {

    private final GameService gameService;

    @GetMapping
    public StatisticsResponse getStatistics() {
        return gameService.getStatistics();
    }
}
