package com.asj.rps.dto;

import com.asj.rps.model.Move;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PlayRoundRequest {
    @NotNull(message = "playerMove must be provided")
    private Move playerMove;
}
