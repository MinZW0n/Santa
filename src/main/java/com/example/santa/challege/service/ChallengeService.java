package com.example.santa.challege.service;


import com.example.santa.challege.dto.ChallengeCreateDto;
import com.example.santa.challege.dto.ChallengeResponseDto;
import com.example.santa.challege.entity.Challenge;

import java.util.List;

public interface ChallengeService {
    //Create
    ChallengeResponseDto saveChallenge(ChallengeCreateDto challengeCreateDto);
    List<Challenge> findAllChallenges();
    ChallengeResponseDto findChallengeById(Long id);

    ChallengeResponseDto updateChallenge(Long id, ChallengeCreateDto challengeCreateDto);

    void deleteChallenge(Long id);


    }
