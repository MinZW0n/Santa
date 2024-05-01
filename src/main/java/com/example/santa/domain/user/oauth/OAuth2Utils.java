package com.example.santa.domain.user.oauth;

import com.example.santa.domain.user.entity.SocialType;

import java.util.Map;

public class OAuth2Utils {
    public static SocialType getSocialType(String registrationId) {
        // user 에서 사용하기 위해 대문자로 변경
        if (registrationId != null) {
            registrationId = registrationId.toUpperCase();
        }

        if ("GOOGLE".equals(registrationId)) {
            return SocialType.GOOGLE;
        } else if ("KAKAO".equals(registrationId)) {
            return SocialType.KAKAO;
        }
        return null;
    }

    public static OAuth2UserInfo getOAuth2UserInfo(SocialType socialType, Map<String, Object> attributes) {
        switch (socialType) {
            case GOOGLE:
                return new GoogleOAuth2UserInfo(attributes);
            case KAKAO:
                return new KakaoOAuth2UserInfo(attributes);
        }
        return null;
    }
}
