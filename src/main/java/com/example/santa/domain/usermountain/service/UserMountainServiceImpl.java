package com.example.santa.domain.usermountain.service;

import ch.qos.logback.classic.Logger;
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
import com.example.santa.domain.usermountain.dto.UserMountainVerifyRequestDto;
import com.example.santa.domain.usermountain.dto.UserMountainVerifyResponseDto;
import com.example.santa.domain.usermountain.entity.UserMountain;
import com.example.santa.domain.usermountain.repository.UserMountainRepository;
import com.example.santa.global.exception.ExceptionCode;
import com.example.santa.global.exception.ServiceLogicException;
import com.example.santa.global.util.mapsturct.UserMountainResponseDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class UserMountainServiceImpl implements UserMountainService {

    private final UserMountainRepository userMountainRepository;
    private final MountainRepository mountainRepository;
    private final UserRepository userRepository;

    private final UserChallengeRepository userChallengeRepository;

    private final CategoryRepository categoryRepository;
    private final UserMountainResponseDtoMapper userMountainResponseDtoMapper;
    private final UserChallengeService userChallengeService;
    private final ChallengeRepository challengeRepository;


    @Autowired
    public UserMountainServiceImpl(UserMountainRepository userMountainRepository
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
        /*
        public UserMountainResponseDto verifyAndCreateUserMountain1(UserMountainVerifyRequestDto userMountainVerifyRequestDto,String email) 생성을 할때
        이런식으로 RequestDto를 던져주면 생성이 되지않습니다.
*/
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
                throw new ServiceLogicException(ExceptionCode.ALREADY_USERMOUNTAIN_ON_DATE);
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

            // UserChallenge 생성 후 호출
            updateProgress(user.getEmail(), userMountain.getId());

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
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.USERMOUNTAIN_NOT_FOUND));
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
        }

    }

    //오류발생
    @Override
    public UserMountainResponseDto verifyAndCreateUserMountain1(UserMountainVerifyRequestDto userMountainVerifyRequestDto,String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.USER_NOT_FOUND));
        Category category = categoryRepository.findByName("기타")
                .orElseThrow(() -> new RuntimeException("기타 카테고리가 등록되지 않았습니다."));
        double distance = 5;
        Optional<Mountain> optionalMountain = mountainRepository.findMountainsWithinDistance(
                userMountainVerifyRequestDto.getLatitude(),
                userMountainVerifyRequestDto.getLongitude(),
                distance);

        if (optionalMountain.isPresent()) {
            Mountain mountain = optionalMountain.get();

            // 여기서 검증 로직 추가
            Optional<UserMountain> existingRecord = userMountainRepository.findByUserAndMountainAndClimbDate(
                    user, mountain, userMountainVerifyRequestDto.getClimbDate());
            if (existingRecord.isPresent()) {
                throw new ServiceLogicException(ExceptionCode.ALREADY_USERMOUNTAIN_ON_DATE);
            }

            UserMountain save = userMountainRepository.save(UserMountain.builder()
                    .latitude(userMountainVerifyRequestDto.getLatitude())
                    .longitude(userMountainVerifyRequestDto.getLongitude())
                    .climbDate(userMountainVerifyRequestDto.getClimbDate())
                    .mountain(mountain)
                    .user(user)
                    .category(category) //기타 카테고리 고정
                    .build());

            double newAccumulatedHeight = user.getAccumulatedHeight() + mountain.getHeight();
            user.setAccumulatedHeight(newAccumulatedHeight);
            userRepository.save(user);

            return userMountainResponseDtoMapper.toDto(save);
        } else {
            throw new IllegalArgumentException("인증에 실패하셨습니다.");
        }
        //>
    }

}

//    //등산한 모든 산 유저쪽으로
//    @Override
//    public List<UserMountainResponseDto> getAllUserMountains() {
//        //유저 테이블에 유저마운틴을 조인해서 가져와라 id: user.getId()
//        List<UserMountain> userMountains = userMountainRepository.findAll();
//        return userMountainResponseDtoMapper.toDtoList(userMountains);
//    }

//    //등산한 산 유저쪽으로
//    @Override
//    public UserMountainResponseDto getUserMountainById(Long id) {
//        UserMountain userMountain = userMountainRepository.findById(id)
//                .orElseThrow(() -> new RuntimeException("UserMountain not found with id: " + id));
//        return userMountainResponseDtoMapper.toDto(userMountain);
//    }