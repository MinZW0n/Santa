package com.example.santa.domain.rank.service;

import com.example.santa.domain.rank.dto.RankingResponseDto;

import java.util.List;
import java.util.Optional;

public interface RankingService {
    void updateAllRanks();

    List<RankingResponseDto> getRankingOrderedByScore();

    Optional<RankingResponseDto> getRankingByEmail(String email);

}
