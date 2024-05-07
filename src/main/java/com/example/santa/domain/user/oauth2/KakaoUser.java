package com.example.santa.domain.user.oauth2;

import com.example.santa.domain.user.entity.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class KakaoUser implements OauthUser {
    @JsonProperty("kakao_account")
    private KakaoAccount kakaoAccount;

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class KakaoAccount {
        private String email;
    }

    @Override
    public String getEmail() {
        return kakaoAccount.getEmail();
    }

    @Override
    public SocialType socialType() {
        return SocialType.KAKAO;
    }
}
