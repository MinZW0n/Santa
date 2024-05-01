package com.example.santa.domain.user.oauth;

import lombok.AllArgsConstructor;

import java.util.Map;

@AllArgsConstructor
public abstract class OAuth2UserInfo {
    // 각 소셜에서 잔달된 전체 정보
    protected Map<String, Object> attributes;

    // 소셜에서 제공받은 정보를 주출하기 위한 메서드
    // 소셜 식별 값 (소셜 내부에서 사용자 식별값)
    public abstract String getSocialId();
    // 최초 로그인 시, 회원가입할 때 이메일을 받기 위해 사용
    public abstract String getEmail();
}
