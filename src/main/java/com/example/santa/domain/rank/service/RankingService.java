package com.example.santa.domain.rank.service;

import com.example.santa.domain.rank.dto.RankingReponseDto;

import java.util.List;

public interface RankingService {
    void updateAllRanks();

    List<RankingReponseDto> getRankingOrderedByScore();
}
