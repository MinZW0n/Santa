package com.example.santa.domain.user.service;

import com.example.santa.domain.user.dto.request.UserSignupRequest;

public interface UserService {
    // create
    Long signup(UserSignupRequest request);

    // duplicate
    Boolean checkEmailDuplicate(String email);

    Boolean checkNicknameDuplicate(String nickname);

}
