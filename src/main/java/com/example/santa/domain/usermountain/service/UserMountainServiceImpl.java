package com.example.santa.domain.usermountain.service;

import com.example.santa.domain.category.entity.Category;
import com.example.santa.domain.category.repository.CategoryRepository;
import com.example.santa.domain.mountain.entity.Mountain;
import com.example.santa.domain.mountain.repository.MountainRepository;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import com.example.santa.domain.usermountain.dto.UserMountainVerifyResponseDto;
import com.example.santa.domain.usermountain.entity.UserMountain;
import com.example.santa.domain.usermountain.repository.UserMountainRepository;
import com.example.santa.global.util.mapsturct.UserMountainResponseDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
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
    public UserMountainResponseDto verifyAndCreateUserMountain(double latitude, double longitude, Date climbDate, String userEmail, Long categoryId) {
        User user = userRepository.findByEmail(userEmail).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        System.out.println(user.getEmail());
        Category category = categoryRepository.findById(categoryId).orElseThrow(() -> new RuntimeException("등록되지 않은 카테고리입니다."));
        double distance = 0.05;
        Optional<Mountain> optionalMountain = mountainRepository.findMountainsWithinDistance(latitude, longitude, distance);


        if (optionalMountain.isPresent()) {
            Mountain mountain = optionalMountain.get();
            UserMountain userMountain = UserMountain.builder()
                    .latitude(latitude)
                    .longitude(longitude)
                    .climbDate(climbDate)
                    .mountain(mountain)
                    .user(user)
                    .category(category)
                    .build();
            UserMountain savedUserMountain = userMountainRepository.save(userMountain);
            return userMountainResponseDtoMapper.toDto(savedUserMountain);
        } else {
            throw new IllegalArgumentException("인증에 실패하셨습니다.");
        }
    }

    //등산한 모든 산
    @Override
    public List<UserMountainResponseDto> getAllUserMountains() {
        List<UserMountain> userMountains = userMountainRepository.findAll();
        return userMountainResponseDtoMapper.toDtoList(userMountains);
    }

    //등산한 산
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