package com.example.santa.domain.usermountain.service;

import com.example.santa.domain.mountain.entity.Mountain;
import com.example.santa.domain.mountain.repository.MountainRepository;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.domain.usermountain.dto.UserMountainRequestDto;
import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import com.example.santa.domain.usermountain.entity.UserMountain;
import com.example.santa.domain.usermountain.repository.UserMountainRepository;
import com.example.santa.global.util.mapsturct.UserMountainResponseDtoMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UserMountainServiceImpl {

    private final UserMountainRepository userMountainRepository;
    private final MountainRepository mountainRepository;
    private final UserRepository userRepository;

    private final UserMountainResponseDtoMapper userMountainResponseDtoMapper;

    @Autowired
    public UserMountainServiceImpl(UserMountainRepository userMountainRepository, MountainRepository mountainRepository, UserRepository userRepository,UserMountainResponseDtoMapper userMountainResponseDtoMapper) {
        this.userMountainRepository = userMountainRepository;
        this.mountainRepository = mountainRepository;
        this.userRepository = userRepository;
        this.userMountainResponseDtoMapper = userMountainResponseDtoMapper;
    }

    @Transactional
    public boolean verify1(UserMountainResponseDto userMountainResponseDto) {
        Mountain mountain = mountainRepository.findById(userMountainResponseDto.getMountainId()).orElse(null);
        User user = userRepository.findById(userMountainResponseDto.getUserId()).orElse(null);

        //사용자,산 null이면 인증X
        if (mountain == null || user == null) {
            return false;
        }
        // 사용자 위도,경도 산 위도 경도 일치검증(오차범위 추후수정)
        if (Math.abs(mountain.getLatitude() - userMountainResponseDto.getLatitude()) <= 0.001 &&
                Math.abs(mountain.getLongitude() - userMountainResponseDto.getLongitude()) <= 0.001) {
            UserMountain userMountain = UserMountain.builder()
                    .user(user)
                    .mountain(mountain)
                    .latitude(userMountainResponseDto.getLatitude()) // 사용자가 제공한 위도 값 저장
                    .longitude(userMountainResponseDto.getLongitude()) // 사용자가 제공한 경도 값 저장
                    .build();
            userMountainRepository.save(userMountain);
            return true;
        }
        return false;
    }

    @Transactional
    public UserMountainResponseDto verify2(Long userId, Long mountainId, double latitude, double longitude) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("유저를 찾을 수 없습니다."));
        Mountain mountain = mountainRepository.findById(mountainId).orElseThrow(() -> new RuntimeException("등록되지 않은 산입니다."));
        if (Math.abs(mountain.getLatitude() - latitude) <= 0.01 && Math.abs(mountain.getLongitude() - longitude) <= 0.01) {
            UserMountain userMountain = UserMountain.builder()
                    .user(user)
                    .mountain(mountain)
                    .latitude(latitude) // 사용자가 제공한 위도 값 저장
                    .longitude(longitude) // 사용자가 제공한 경도 값 저장
                    .build();

            UserMountain savedUserMountain = userMountainRepository.save(userMountain);
            return userMountainResponseDtoMapper.toDto(savedUserMountain);
        } else {
            throw new RuntimeException("인증할 수 없는 위치입니다.");
        }
    }

//    @Transactional
//    public UserMountainResponseDto verify2(UserMountainRequestDto userMountainRequestDto) {
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
//    }


    public List<UserMountainResponseDto> getAllUserMountains() {
        List<UserMountain> userMountains = userMountainRepository.findAll();
        return userMountainResponseDtoMapper.toDtoList(userMountains);
    }

    public UserMountainResponseDto getUserMountainById(Long id) {
        UserMountain userMountain = userMountainRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("UserMountain not found with id: " + id));
        return userMountainResponseDtoMapper.toDto(userMountain);
    }
}
