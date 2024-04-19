package com.example.santa.domain.user.service;

import com.example.santa.domain.user.dto.request.UserSignupRequest;
import com.example.santa.domain.user.entity.Password;
import com.example.santa.domain.user.entity.Role;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import jakarta.persistence.EntityExistsException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Transactional
@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

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
    public Long signup(UserSignupRequest request) {
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
                .role(Role.USER)
                .build();

        User save = userRepository.save(user);
        return save.getId();
    }
}
