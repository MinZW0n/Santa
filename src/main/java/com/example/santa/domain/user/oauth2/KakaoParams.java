package com.example.santa.domain.user.oauth2;

import com.example.santa.domain.user.entity.SocialType;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class KakaoParams implements OauthParams {
    private String authorizationCode;

    @Override
    public SocialType socialType() {
        return SocialType.KAKAO;
    }

    @Override
    public MultiValueMap<String, String> makeBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("code", authorizationCode);
        return body;
    }

    @Override
    public String getAuthorizationCode() {
        return authorizationCode;
    }
}
