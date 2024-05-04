package com.example.santa.domain.user.oauth;

import com.example.santa.domain.user.entity.SocialType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.util.Map;

@Getter
@Builder
@AllArgsConstructor
public class OAuth2Attributes {
    // OAuth2 로그인 시 사용자 정보가 담긴 Key 값
    private String nameAttributeKey;
    private OAuth2UserInfo oAuth2UserInfo;

    public static OAuth2Attributes of(SocialType socialType, String userNameAttributeName, Map<String, Object> attributes) {
        return OAuth2Attributes.builder()
                .nameAttributeKey(userNameAttributeName)
                .oAuth2UserInfo(OAuth2Utils.getOAuth2UserInfo(socialType, attributes))
                .build();
    }
}
