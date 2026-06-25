package com.asj.rps.entity;

import com.asj.rps.model.Move;
import com.asj.rps.model.RoundResult;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;

@Entity
@Table(name = "game_rounds")
@Getter
@Setter
@NoArgsConstructor
public class GameRound {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private Move playerMove;

    @Enumerated(EnumType.STRING)
    private Move computerMove;

    @Enumerated(EnumType.STRING)
    private RoundResult result;

    private Instant playedAt;

}
