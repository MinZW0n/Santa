package com.example.santa.domain.user.service;

import com.example.santa.domain.user.dto.UserResponseDto;
import com.example.santa.domain.user.dto.UserSignInRequestDto;
import com.example.santa.domain.user.dto.UserSignupRequestDto;
import com.example.santa.domain.user.dto.UserUpdateRequestDto;
import com.example.santa.domain.user.entity.Password;
import com.example.santa.domain.user.entity.Role;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import com.example.santa.domain.usermountain.entity.UserMountain;
import com.example.santa.global.security.jwt.JwtToken;
import com.example.santa.global.security.jwt.JwtTokenProvider;
import com.example.santa.global.util.mapsturct.UserMountainResponseDtoMapper;
import com.example.santa.global.util.mapsturct.UserResponseDtoMapper;
import jakarta.persistence.EntityExistsException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserResponseDtoMapper userResponseDtoMapper;
    private final AuthenticationManagerBuilder authenticationManagerBuilder;
    private final JwtTokenProvider jwtTokenProvider;

    private final UserMountainResponseDtoMapper userMountainResponseDtoMapper;

    @Transactional
    @Override
    public Long signup(UserSignupRequestDto request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new EntityExistsException("이메일 중복입니다.");
        }
        if (userRepository.existsByNickname(request.getNickname())) {
            throw new EntityExistsException("닉네임 중복입니다.");
        }

        User user = User.builder()
                .email(request.getEmail())
                .password(new Password(request.getPassword()))
                .name(request.getName())
                .nickname(request.getNickname())
                .phoneNumber(request.getPhoneNumber())
                .role(Role.USER)
                .build();

        User save = userRepository.save(user);
        return save.getId();
    }

    @Override
    public Boolean checkEmailDuplicate(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new EntityExistsException("이메일 중복입니다.");
        }
        return true;
    }

    @Override
    public Boolean checkNicknameDuplicate(String nickname) {
        if (userRepository.existsByNickname(nickname)) {
            throw new EntityExistsException("닉네임 중복입니다");
        }
        return true;
    }

    /*
     * email + password 를 받아서 Authentication 객체 생성
     * authenticate 메서드 를 사용해서 User 검증 -> 이때 만들어 놓은 loadUserByUsername 메서드가 실행 됨
     * 검증 성공하면 Authentication 객체를 기반으로 JWT 토큰 생성
     * */
    @Transactional
    @Override
    public JwtToken signIn(UserSignInRequestDto userSignInRequestDto) {
//        User user = userRepository.findByEmail(userSignInRequestDto.getEmail())
//                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. userEmail=" + userSignInRequestDto.getEmail()));
        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(userSignInRequestDto.getEmail(), userSignInRequestDto.getPassword());
        authenticationManagerBuilder.getObject().authenticate(authenticationToken);
//        User user = userRepository.findByEmail(userSignInRequestDto.getEmail()).orElseThrow();
        JwtToken jwtToken = jwtTokenProvider.generateToken(authenticationToken);
        return jwtToken;
    }

    @Override
    public UserResponseDto findUserByEmail(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. userEmail=" + email));
        UserResponseDto dto = userResponseDtoMapper.toDto(user);
        log.info("dto = {}", dto);
        return dto;
    }

    @Override
    public Page<UserResponseDto> findAllUser(Pageable pageable) {
        return userRepository.findAll(pageable).map(userResponseDtoMapper::toDto);
    }


    @Override
    public UserResponseDto updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto) {
        if (userRepository.existsByNickname(userUpdateRequestDto.getNickname())) {
            throw new EntityExistsException("닉네임 중복입니다");
        }
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. userId=" + id))
                .update(userUpdateRequestDto.getName()
                        , userUpdateRequestDto.getNickname()
                        , userUpdateRequestDto.getPhoneNumber()
                        , userUpdateRequestDto.getImage());

        return userResponseDtoMapper.toDto(user);
    }

    @Override
    public String changePassword(String email, String oldPassword, String newPassword) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. userEmail=" + email));
        user.getPasswordForChange().changePassword(oldPassword, newPassword);
        return user.getEmail();
    }

    @Override
    public Page<UserMountainResponseDto> findAllUserMountains(String email, Pageable pageable) {
        User byEmail = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. userEmail=" + email));
        Page<UserMountainResponseDto> pageDto = userRepository.findUserMountainsByUserId(byEmail.getId(), pageable)
                .map(userMountainResponseDtoMapper::toDto);
        return pageDto;
    }

//    @Override
//    public String findPassword(String email, String newPassword) {
//
//    }


}
