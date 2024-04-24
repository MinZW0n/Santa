package com.example.santa.domain.usermountain.service;

import ch.qos.logback.classic.Logger;
import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.category.repository.CategoryRepository;
import com.example.santa.domain.mountain.entity.Mountain;
import com.example.santa.domain.mountain.repository.MountainRepository;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
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

    private final CategoryRepository categoryRepository;
    private final UserMountainResponseDtoMapper userMountainResponseDtoMapper;

    @Autowired
    public UserMountainServiceImpl(UserMountainRepository userMountainRepository, MountainRepository mountainRepository, UserRepository userRepository,CategoryRepository categoryRepository,UserMountainResponseDtoMapper userMountainResponseDtoMapper) {
        this.userMountainRepository = userMountainRepository;
        this.mountainRepository = mountainRepository;
        this.userRepository = userRepository;
        this.categoryRepository = categoryRepository;
        this.userMountainResponseDtoMapper = userMountainResponseDtoMapper;
    }
    //산 인증
    @Override
    @Transactional
    public UserMountainResponseDto verifyAndCreateUserMountain(double latitude, double longitude, LocalDate climbDate, String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new ServiceLogicException(ExceptionCode.USER_NOT_FOUND));
        Category category = categoryRepository.findByName("기타")
                .orElseThrow(() -> new RuntimeException("기타 카테고리가 등록되지 않았습니다."));
        double distance = 5;
        Optional<Mountain> optionalMountain = mountainRepository.findMountainsWithinDistance(
                latitude,
                longitude,
                distance);
        if (optionalMountain.isPresent()) {
            Mountain mountain = optionalMountain.get();
            // 여기서 검증 로직 추가
            Optional<UserMountain> existingRecord = userMountainRepository.findByUserAndMountainAndClimbDate(
                    user, mountain, climbDate);
            if (existingRecord.isPresent()) {
                throw new IllegalStateException("이미 같은 날에 이 산에 대한 인증이 존재합니다.");
            }

            UserMountain save = userMountainRepository.save(UserMountain.builder()
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
            log.info("mountain {}", save.getMountain());
            log.info("user {}", save.getUser());
            log.info("user {}", save.getCategory());
            return userMountainResponseDtoMapper.toDto(save);
        } else {
            throw new IllegalArgumentException("인증에 실패하셨습니다.");
        }
        //>
    }

    //오류발생  java.lang.RuntimeException: 유저를 찾을 수 없습니다. 완성코드
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
                throw new IllegalStateException("이미 같은 날에 이 산에 대한 인증이 존재합니다.");
            }

            log.info("mountain {}", mountain);
            log.info("user {}", user);
            log.info("category {}", category);
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

    //등산한 모든 산 유저쪽으로
    @Override
    public List<UserMountainResponseDto> getAllUserMountains() {
        //유저 테이블에 유저마운틴을 조인해서 가져와라 id: user.getId()
        List<UserMountain> userMountains = userMountainRepository.findAll();
        return userMountainResponseDtoMapper.toDtoList(userMountains);
    }

    //등산한 산 유저쪽으로
    @Override
    public UserMountainResponseDto getUserMountainById(Long id) {
        UserMountain userMountain = userMountainRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserMountain not found with id: " + id));
        return userMountainResponseDtoMapper.toDto(userMountain);
    }


    @Override
    @Transactional
    public Mountain test(double latitude, double longitude) {
        Mountain mountainsWithinDistance = mountainRepository.findMountainsWithinDistance(latitude, longitude, 0.05)
                .orElseThrow(() -> new IllegalArgumentException("등록되지 않은 산입니다."));

        return mountainsWithinDistance;
        //설악산이 맞습니까? 이런식으로 로직이 추가되어야
    }


    //코치님 피드백 적용
//
//    @Override
//    public UserMountainResponseDto verifyAndCreateUserMountain1(UserMountainVerifyRequestDto userMountainVerifyRequestDto) {
////        //마운틴
////        //유저 부분 토큰에서 검증되기때문에 필요없다
////        //<마운틴이 가져간다
////        Category category = categoryRepository.findById(userMountainVerifyRequestDto.getCategoryId()).orElseThrow(() -> new RuntimeException("등록되지 않은 카테고리입니다."));
////        double distance = 0.05;
//        Optional<Mountain> optionalMountain = mountainRepository.findMountainsWithinDistance(userMountainVerifyRequestDto.getLatitude(),userMountainVerifyRequestDto.getLongitude() , distance);
////        Mountain mountain = optionalMountain.get();
////
////        //
////        //
////
//        //<유지
//        //마운틴 컨트롤러에서 얘를 호출
//        if (optionalMountain.isPresent()) {
//            UserMountain userMountain = UserMountain.builder()
//                    .latitude(userMountainVerifyRequestDto.getLatitude())
//                    .longitude(userMountainVerifyRequestDto.getLongitude())
//                    .climbDate(userMountainVerifyRequestDto.getClimbDate())
//                    .mountain(mountain)
//                    .user(user)
//                    .category(category)
//                    .build();
//            UserMountain savedUserMountain = userMountainRepository.save(userMountain);
//
//            // 누적 높이
//            double newAccumulatedHeight = user.getAccumulatedHeight() + mountain.getHeight();
//            user.setAccumulatedHeight(newAccumulatedHeight);
//            userRepository.save(user); //user엔티티에 저장해도되나?
//
//            return userMountainResponseDtoMapper.toDto(savedUserMountain);
//        } else {
//            throw new IllegalArgumentException("인증에 실패하셨습니다.");
//        }
//        //>
//    }
}

//    1차 완성 코드
//    @Override
//    @Transactional
//    public UserMountainResponseDto verify2(Long userId, Long mountainId, double latitude, double longitude) {
//        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
//        Mountain mountain = mountainRepository.findById(mountainId).orElseThrow(() -> new RuntimeException("등록되지 않은 산입니다."));
//        if (Math.abs(mountain.getLatitude() - latitude) <= 0.01 && Math.abs(mountain.getLongitude() - longitude) <= 0.01) {
//            UserMountain userMountain = UserMountain.builder()
//                    .user(user)
//                    .mountain(mountain)
//                    .latitude(latitude) // 사용자가 제공한 위도 값 저장
//                    .longitude(longitude) // 사용자가 제공한 경도 값 저장
//                    .build();
//
//            UserMountain savedUserMountain = userMountainRepository.save(userMountain);
//            return userMountainResponseDtoMapper.toDto(savedUserMountain);
//        } else {
//            throw new RuntimeException("인증할 수 없는 위치입니다.");
//        }
//
//        //설악산이 맞습니까? 이런식으로 로직이 추가되어야
//    }


//    @Transactional
//    public boolean verify1(UserMountainResponseDto userMountainResponseDto) {
//        Mountain mountain = mountainRepository.findById(userMountainResponseDto.getMountainId()).orElse(null);
//        User user = userRepository.findById(userMountainResponseDto.getUserId()).orElse(null);
//
//        //사용자,산 null이면 인증X
//        if (mountain == null || user == null) {
//            return false;
//        }
//        // 사용자 위도,경도 산 위도 경도 일치검증(오차범위 추후수정)
//        if (Math.abs(mountain.getLatitude() - userMountainResponseDto.getLatitude()) <= 0.001 &&
//                Math.abs(mountain.getLongitude() - userMountainResponseDto.getLongitude()) <= 0.001) {
//            UserMountain userMountain = UserMountain.builder()
//                    .user(user)
//                    .mountain(mountain)
//                    .latitude(userMountainResponseDto.getLatitude()) // 사용자가 제공한 위도 값 저장
//                    .longitude(userMountainResponseDto.getLongitude()) // 사용자가 제공한 경도 값 저장
//                    .build();
//            userMountainRepository.save(userMountain);
//            return true;
//        }
//        return false;
//    }