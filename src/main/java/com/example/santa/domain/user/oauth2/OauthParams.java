package com.example.santa.domain.user.oauth2;

import com.example.santa.domain.user.entity.SocialType;
import org.springframework.util.MultiValueMap;

public interface OauthParams {
    public SocialType socialType();
    public String getAuthorizationCode();
    public MultiValueMap<String, String> makeBody();
}
