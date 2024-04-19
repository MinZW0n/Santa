package com.example.santa.challege.service;

import com.example.santa.challege.dto.ChallengeCreateDto;
import com.example.santa.challege.dto.ChallengeResponseDto;
import com.example.santa.challege.entity.Challenge;
import com.example.santa.challege.mapper.ChallengeMapper;
import com.example.santa.challege.repository.ChallengeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ChallengeServiceImpl implements ChallengeService{

    private final ChallengeRepository challengeRepository;
    private final ChallengeMapper challengeMapper;

    @Autowired
    public ChallengeServiceImpl(ChallengeRepository challengeRepository, ChallengeMapper challengeMapper) {
        this.challengeRepository = challengeRepository;
        this.challengeMapper = challengeMapper;
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
        return challengeMapper.entityToDto(save);
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
                .map(challengeMapper::entityToDto)
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
            result = challengeMapper.entityToDto(challenge);
        }
        return result;
    }

    @Override
    public void deleteChallenge(Long id) {

        challengeRepository.deleteById(id);
    }
}