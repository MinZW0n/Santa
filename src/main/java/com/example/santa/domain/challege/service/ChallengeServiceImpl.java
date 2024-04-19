package com.example.santa.domain.challege.service;

import com.example.santa.domain.challege.dto.ChallengeCreateDto;
import com.example.santa.domain.challege.dto.ChallengeResponseDto;
import com.example.santa.domain.challege.entity.Challenge;
import com.example.santa.global.util.mapsturct.ChallengeResponseMapper;
import com.example.santa.domain.challege.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChallengeServiceImpl implements ChallengeService{

    private final ChallengeRepository challengeRepository;
    private final ChallengeResponseMapper challengeResponseMapper;

    @Autowired
    public ChallengeServiceImpl(ChallengeRepository challengeRepository, ChallengeResponseMapper challengeResponseMapper) {
        this.challengeRepository = challengeRepository;
        this.challengeResponseMapper = challengeResponseMapper;
    }


    //CREATE
    @Transactional
    @Override
    public ChallengeResponseDto saveChallenge(ChallengeCreateDto challengeCreateDto) {
        Challenge save = challengeRepository.save(Challenge.builder()
                .name(challengeCreateDto.getName())
                .description(challengeCreateDto.getDescription())
                .clearStandard(challengeCreateDto.getClearStandard())
                .image(challengeCreateDto.getImage())
                .build());
        return challengeResponseMapper.toDto(save);
    }

//    @Override
//    public List<ChallengeResponseDto> findAllChallenges() {
//        List<Challenge> challenges = challengeRepository.findAll();
//        List<ChallengeResponseDto> challengeDtos = new ArrayList<>();
//        for (Challenge challenge : challenges) {
//            challengeDtos.add(challengeMapper.entityToDto(challenge));
//        }
//        return challengeDtos;
//    }

    @Override
    public List<Challenge> findAllChallenges() {
        return challengeRepository.findAll();
    }

    @Override
    public ChallengeResponseDto findChallengeById(Long id) {
        return challengeRepository.findById(id)
                .map(challengeResponseMapper::toDto)
                .orElse(null);
    }


    @Override
    public ChallengeResponseDto updateChallenge(Long id, ChallengeCreateDto challengeCreateDto) {
        ChallengeResponseDto result = null;
        if (challengeRepository.existsById(id)) {
            Challenge challenge = challengeRepository.findById(id).get();
            challenge.setName(challengeCreateDto.getName());
            challenge.setDescription(challengeCreateDto.getDescription());
            challenge.setImage(challengeCreateDto.getImage());
            challenge.setClearStandard(challengeCreateDto.getClearStandard());
            challenge = challengeRepository.save(challenge);
            result = challengeResponseMapper.toDto(challenge);
        }
        return result;
    }

    @Override
    public void deleteChallenge(Long id) {

        challengeRepository.deleteById(id);
    }
}