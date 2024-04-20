package com.example.santa.domain.challege.service;

import com.example.santa.domain.challege.dto.ChallengeCreateDto;
import com.example.santa.domain.challege.dto.ChallengeResponseDto;
import com.example.santa.domain.challege.entity.Challenge;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.domain.userchallenge.dto.UserChallengeResponseDto;
import com.example.santa.domain.userchallenge.entity.UserChallenge;
import com.example.santa.domain.userchallenge.repository.UserChallengeRepository;
import com.example.santa.global.util.mapsturct.ChallengeResponseMapper;
import com.example.santa.domain.challege.repository.ChallengeRepository;
import com.example.santa.global.util.mapsturct.UserChallengeResponseMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ChallengeServiceImpl implements ChallengeService{

    private final ChallengeRepository challengeRepository;
    private final ChallengeResponseMapper challengeResponseMapper;

    private final UserChallengeResponseMapper userChallengeResponseMapper;

    private final UserRepository userRepository;

    private final UserChallengeRepository userChallengeRepository;

    @Autowired
    public ChallengeServiceImpl(ChallengeRepository challengeRepository, UserChallengeRepository userChallengeRepository, UserRepository userRepository, ChallengeResponseMapper challengeResponseMapper, UserChallengeResponseMapper userChallengeResponseMapper) {
        this.challengeRepository = challengeRepository;
        this.userChallengeRepository = userChallengeRepository;
        this.userRepository = userRepository;
        this.challengeResponseMapper = challengeResponseMapper;
        this.userChallengeResponseMapper = userChallengeResponseMapper;
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


    @Override
    public Page<ChallengeResponseDto> findAllChallenges(Pageable pageable){
        Page<Challenge> challenges =challengeRepository.findAll(pageable);
        return challenges.map(challengeResponseMapper::toDto);
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


//    @Transactional
//    public UserChallengeResponseDto updateUserChallengeProgress(Long userId, Long challengeId, String progress, Boolean isCompleted) {
//        // 사용자와 챌린지를 찾는 로직 구현
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
//        Challenge challenge = challengeRepository.findById(challengeId).orElseThrow(() -> new RuntimeException("Challenge not found"));
//
//        UserChallenge userChallenge = userChallengeRepository.findByUserAndChallenge(user, challenge)
//                .orElse(new UserChallenge());
//
//        userChallenge.setUser(user);
//        userChallenge.setChallenge(challenge);
//        userChallenge.setProgress(progress);
//        userChallenge.setIsCompleted(isCompleted);
//
//        // 완료 여부에 따른 완료 날짜 처리
//        if (isCompleted) {
//            userChallenge.setCompletionDate(new Date());
//        } else {
//            userChallenge.setCompletionDate(null);
//        }
//
//        userChallengeRepository.save(userChallenge);
//
//        // DTO로 변환하여 반환
//        return  userChallengeResponseMapper.toDto(userChallenge);
//    }
//
//    private UserChallengeDto userChallengeToDto(UserChallenge userChallenge) {
//        UserChallengeDto dto = new UserChallengeDto();
//        dto.setProgress(userChallenge.getProgress());
//        dto.setIsCompleted(userChallenge.getIsCompleted());
//        dto.setCompletionDate(userChallenge.getCompletionDate());
//        // 필요한 다른 필드들도 여기에 추가
//        return dto;
//    }
//
//    // 사용자가 참여중인 챌린지 조회, 이때 진행도와 완료 여부도 함께 보여줌
//    @Transactional(readOnly = true)
//    public List<UserChallengeDto> getUserChallenges(Long userId) {
//        List<UserChallenge> userChallenges = userChallengeRepository.findByUserId(userId);
//        return userChallenges.stream().map(this::userChallengeToDto).collect(Collectors.toList());
//    }

}


//    @Override
//    public List<ChallengeResponseDto> findAllChallenges() {
//        List<Challenge> challenges = challengeRepository.findAll();
//        List<ChallengeResponseDto> challengeDtos = new ArrayList<>();
//        for (Challenge challenge : challenges) {
//            challengeDtos.add(challengeResponseMapper.toDto(challenge));
//        }
//        return challengeDtos;
//    }

//    @Override
//    public List<Challenge> findAllChallenges() {
//        return challengeRepository.findAll();
//    }