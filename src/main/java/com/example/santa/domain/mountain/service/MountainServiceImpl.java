package com.example.santa.domain.mountain.service;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.category.repository.CategoryRepository;
import com.example.santa.domain.challege.entity.Challenge;
import com.example.santa.domain.challege.repository.ChallengeRepository;
import com.example.santa.domain.mountain.entity.Mountain;
import com.example.santa.domain.mountain.repository.MountainRepository;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.domain.userchallenge.entity.UserChallenge;
import com.example.santa.domain.userchallenge.repository.UserChallengeRepository;
import com.example.santa.domain.userchallenge.service.UserChallengeService;
import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import com.example.santa.domain.usermountain.entity.UserMountain;
import com.example.santa.domain.usermountain.repository.UserMountainRepository;
import com.example.santa.global.exception.ExceptionCode;
import com.example.santa.global.exception.ServiceLogicException;
import com.example.santa.global.util.mapsturct.UserMountainResponseDtoMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
/*
@Service
@RequiredArgsConstructor
public class MountainServiceImpl implements MountainService{

    private final MountainRepository mountainRepository;
    private final UserMountainRepository userMountainRepository;
    private final MountainRepository mountainRepository;
    private final UserRepository userRepository;

    private final UserChallengeRepository userChallengeRepository;

    private final CategoryRepository categoryRepository;
    private final UserMountainResponseDtoMapper userMountainResponseDtoMapper;
    private final UserChallengeService userChallengeService;
    private final ChallengeRepository challengeRepository;


    @Autowired
    public MountainServiceImpl(UserMountainRepository userMountainRepository
            , MountainRepository mountainRepository
            , UserRepository userRepository
            ,CategoryRepository categoryRepository
            ,UserChallengeRepository userChallengeRepository
            ,ChallengeRepository challengeRepository
            ,UserChallengeService userChallengeService
            ,UserMountainResponseDtoMapper userMountainResponseDtoMapper) {
        this.userMountainRepository = userMountainRepository;
        this.mountainRepository = mountainRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.userChallengeRepository = userChallengeRepository;
        this.userChallengeService =userChallengeService;
        this.challengeRepository =challengeRepository;
        this.userMountainResponseDtoMapper = userMountainResponseDtoMapper;
    }

    @Override
    @Transactional
    public UserMountainResponseDto verifyAndCreateUserMountain(double latitude, double longitude, LocalDate climbDate, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.USER_NOT_FOUND));
        Category category = categoryRepository.findByName("기타")
                .orElseThrow(() -> new RuntimeException("기타 카테고리가 등록되지 않았습니다."));

        double distance = 5;
        Optional<Mountain> optionalMountain = mountainRepository.findMountainsWithinDistance(
                latitude, longitude, distance);
        if (optionalMountain.isPresent()) {
            Mountain mountain = optionalMountain.get();

            Optional<UserMountain> existingRecord = userMountainRepository.findByUserAndMountainAndClimbDate(
                    user, mountain, climbDate);
            if (existingRecord.isPresent()) {
                throw new IllegalStateException("이미 같은 날에 이 산에 대한 인증이 존재합니다.");
            }

            UserMountain userMountain = userMountainRepository.save(UserMountain.builder()
                    .latitude(latitude)
                    .longitude(longitude)
                    .climbDate(climbDate)
                    .mountain(mountain)
                    .user(user)
                    .category(category) //기타 카테고리 고정
                    .build());

            double newAccumulatedHeight = user.getAccumulatedHeight() + mountain.getHeight();
            user.setAccumulatedHeight(newAccumulatedHeight);
            userRepository.save(user);

            // UserChallenge 진행 상태 업데이트
            updateProgress(email, userMountain.getId());

            return userMountainResponseDtoMapper.toDto(userMountain);
            // UserChallenge 진행 상태 업데이트
        } else {
            throw new IllegalArgumentException("인증에 실패하셨습니다.");
        }
    }

    public void updateProgress(String email, Long userMountainId) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.USER_NOT_FOUND));
        UserMountain userMountain = userMountainRepository.findById(userMountainId)
                .orElseThrow(() -> new IllegalArgumentException("유저 마운틴을 찾을 수 없습니다."));
        Category userMountainCategory = userMountain.getCategory();
        List<Challenge> challenges = challengeRepository.findByCategoryName(userMountainCategory.getName());


        for (Challenge challenge : challenges) {
            UserChallenge userChallenge = userChallengeRepository.findByUserAndChallengeId(user, challenge.getId())
                    .orElseGet(() -> {
                        // 새로운 UserChallenge 생성
                        UserChallenge newUserChallenge = UserChallenge.builder()
                                .user(user)
                                .challenge(challenge)
                                .progress(0) // 초기 진행 상태는 0
                                .build();
                        return userChallengeRepository.save(newUserChallenge);
                    });

            // progress 증가
            userChallenge.setProgress(userChallenge.getProgress() + 1);

            // clearStandard와 progress가 일치하면 성공 처리
            if (userChallenge.getProgress().equals(challenge.getClearStandard())) {
                userChallenge.setIsCompleted(true);
                userChallenge.setCompletionDate(LocalDate.now()); // 성공일자는 현재 날짜로 설정
            }

            userChallengeRepository.save(userChallenge);
            log.info("userChallenge {}", userChallenge);

        }

    }


}
*/