package com.example.santa.domain.usermountain.service;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.category.repository.CategoryRepository;
import com.example.santa.domain.mountain.entity.Mountain;
import com.example.santa.domain.mountain.repository.MountainRepository;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.domain.userchallenge.service.UserChallengeService;
import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import com.example.santa.domain.usermountain.dto.UserMountainVerifyRequestDto;
import com.example.santa.domain.usermountain.entity.UserMountain;
import com.example.santa.domain.usermountain.repository.UserMountainRepository;
import com.example.santa.global.exception.ExceptionCode;
import com.example.santa.global.exception.ServiceLogicException;
import com.example.santa.global.util.mapsturct.UserMountainResponseDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.Optional;

@Service
@Slf4j
public class UserMountainServiceImpl implements UserMountainService {

    private final UserMountainRepository userMountainRepository;
    private final MountainRepository mountainRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final UserMountainResponseDtoMapper userMountainResponseDtoMapper;
    private final UserChallengeService userChallengeService;

    @Autowired
    public UserMountainServiceImpl(UserMountainRepository userMountainRepository
            , MountainRepository mountainRepository
            , UserRepository userRepository
            ,CategoryRepository categoryRepository
            ,UserChallengeService userChallengeService
            ,UserMountainResponseDtoMapper userMountainResponseDtoMapper) {
        this.userMountainRepository = userMountainRepository;
        this.mountainRepository = mountainRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.userChallengeService =userChallengeService;
        this.userMountainResponseDtoMapper = userMountainResponseDtoMapper;
    }

    @Transactional
    @Override
    public UserMountainResponseDto verifyAndCreateUserMountain(UserMountainVerifyRequestDto userMountainVerifyRequestDto,String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.USER_NOT_FOUND));
        Category category = categoryRepository.findByName("기타")
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.OTHER_CATEGORY_CATEGORY_NOT_FOUND));
        double distance = 5;
        Optional<Mountain> optionalMountain = mountainRepository.findMountainsWithinDistance(
                userMountainVerifyRequestDto.getLatitude(),
                userMountainVerifyRequestDto.getLongitude(),
                distance);

        if (optionalMountain.isPresent()) {
            Mountain mountain = optionalMountain.get();

            // 검증 로직
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

            // UserChallenge 생성 후 호출
            userChallengeService.updateProgress(user.getEmail(), save.getId());

            return userMountainResponseDtoMapper.toDto(save);
        } else {
            throw new ServiceLogicException(ExceptionCode.INVALID_USER_LOCATION);
        }
    }

}