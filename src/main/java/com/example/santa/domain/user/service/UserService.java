package com.example.santa.domain.user.service;

import com.example.santa.domain.user.dto.UserResponseDto;
import com.example.santa.domain.user.dto.UserSignupRequestDto;
import com.example.santa.domain.user.dto.UserUpdateRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    // create
    Long signup(UserSignupRequestDto request);
    // duplicate
    Boolean checkEmailDuplicate(String email);
    Boolean checkNicknameDuplicate(String nickname);

    // read
    UserResponseDto findUserById(Long id);

    // Users read(관리자)
    Page<UserResponseDto> findAllUser(Pageable pageable);

    UserResponseDto updateUser(Long id, UserUpdateRequestDto userUpdateRequestDto);

    Long changePassword(Long id, String oldPassword, String newPassword);

}
