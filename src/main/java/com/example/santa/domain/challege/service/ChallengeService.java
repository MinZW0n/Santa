package com.example.santa.domain.challege.service;


import com.example.santa.domain.challege.dto.ChallengeCreateDto;
import com.example.santa.domain.challege.dto.ChallengeResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


public interface ChallengeService {

    ChallengeResponseDto saveChallenge(ChallengeCreateDto challengeCreateDto);
    Page<ChallengeResponseDto> findAllChallenges(Pageable pageable);

    ChallengeResponseDto findChallengeById(Long id);

    ChallengeResponseDto updateChallenge(Long id, ChallengeCreateDto challengeCreateDto);

    void deleteChallenge(Long id);


    }
