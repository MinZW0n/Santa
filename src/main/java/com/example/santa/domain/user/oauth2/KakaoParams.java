package com.example.santa.domain.user.oauth2;

import com.example.santa.domain.user.entity.SocialType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

@Getter
public class KakaoParams implements OauthParams {
    @Schema(description = "테스트 값", example = "y3iAYHVeKZaUfkfAdmKAPC520fAAABj1aMAwYq3eF1vjqPRg")
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
