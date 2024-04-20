package com.example.santa.domain.user.service;

import com.example.santa.domain.user.dto.UserResponseDto;
import com.example.santa.domain.user.dto.UserSignupRequestDto;
import com.example.santa.domain.user.dto.UserUpdateRequestDto;
import com.example.santa.domain.user.entity.Password;
import com.example.santa.domain.user.entity.Role;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.global.util.mapsturct.UserResponseDtoMapper;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final UserResponseDtoMapper userResponseDtoMapper;

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

    @Override
    public UserResponseDto findUserById(Long id) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. userId=" + id));
        UserResponseDto dto = userResponseDtoMapper.toDto(user);
        System.out.println("dto = " + dto.getEmail());
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
    public Long changePassword(Long id, String oldPassword, String newPassword) {
        User user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("해당 유저가 없습니다. userId=" + id));
        user.getPassword().changePassword(oldPassword, newPassword);
        return user.getId();
    }

}
