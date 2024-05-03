package com.example.santa.domain.rank.service;

import com.example.santa.domain.rank.dto.RankingResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface RankingService {

    void updateAllRanks();
    Page<RankingResponseDto> getRankingOrderedByScore(Pageable pageable);
    Optional<RankingResponseDto> getRankingByEmail(String email);

}
