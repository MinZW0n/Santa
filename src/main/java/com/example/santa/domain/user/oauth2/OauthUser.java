package com.example.santa.domain.user.oauth2;

import com.example.santa.domain.user.entity.SocialType;

public interface OauthUser {
    String getEmail();

    SocialType socialType();
}
