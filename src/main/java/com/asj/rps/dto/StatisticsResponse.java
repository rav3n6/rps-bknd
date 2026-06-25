package com.asj.rps.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class StatisticsResponse {
    private long totalRounds;
    private long wins;
    private long losses;
    private long draws;
    private double winRate;
}
