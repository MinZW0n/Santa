package com.example.santa.domain.user.oauth2;

import com.example.santa.domain.user.entity.SocialType;

public interface OauthClient {
    public SocialType socialType();

    public String getOauthLonginToken(OauthParams oauthParams);

    public OauthUser getMemberInfo(String accessToken);
}
