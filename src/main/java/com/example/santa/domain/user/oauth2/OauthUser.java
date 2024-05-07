package com.example.santa.domain.user.oauth2;

import com.example.santa.domain.user.entity.SocialType;

public interface OauthUser {
    public String getEmail();

    SocialType socialType();
}
