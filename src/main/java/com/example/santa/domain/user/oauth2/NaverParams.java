package com.example.santa.domain.user.oauth2;

import com.example.santa.domain.user.entity.SocialType;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class NaverParams implements OauthParams{
    private String authorizationCode;
    private String state;


    @Override
    public SocialType socialType() {
        return SocialType.NAVER;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        body.add("state", state);
        return body;
    }

    @Override
    public String getAuthorizationCode() {
        return authorizationCode;
    }
}

