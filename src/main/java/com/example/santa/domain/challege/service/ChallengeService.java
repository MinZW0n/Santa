package com.example.santa.domain.challege.service;


import com.example.santa.domain.challege.dto.ChallengeCreateDto;
import com.example.santa.domain.challege.dto.ChallengeParticipationResponseDto;
import com.example.santa.domain.challege.dto.ChallengeResponseDto;
import com.example.santa.domain.challege.entity.Challenge;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChallengeService {
    //Create
    ChallengeResponseDto saveChallenge(ChallengeCreateDto challengeCreateDto);
    Page<ChallengeResponseDto> findAllChallenges(Pageable pageable);

    ChallengeResponseDto findChallengeById(Long id);

    List<ChallengeParticipationResponseDto> getUsersParticipationInChallenges();

    ChallengeResponseDto updateChallenge(Long id, ChallengeCreateDto challengeCreateDto);

    void deleteChallenge(Long id);


    }
