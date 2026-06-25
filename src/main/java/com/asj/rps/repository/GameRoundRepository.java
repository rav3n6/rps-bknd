package com.asj.rps.repository;

import com.asj.rps.entity.GameRound;
import com.asj.rps.model.RoundResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface GameRoundRepository extends JpaRepository<GameRound, Long> {
    List<GameRound> findAllByOrderByPlayedAtDesc();
    long countByResult(RoundResult result);
}
