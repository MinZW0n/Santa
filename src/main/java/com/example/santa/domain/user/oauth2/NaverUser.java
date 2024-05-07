package com.example.santa.domain.user.oauth2;

import com.example.santa.domain.user.entity.SocialType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class NaverUser implements OauthUser {
    @JsonProperty("response")
    private Response response;

    @Override
    public String getEmail() {
        return response.email;
    }

    @Override
    public SocialType socialType() {
        return SocialType.NAVER;
    }

    @Getter
    @JsonIgnoreProperties(ignoreUnknown = true)
    public class Response {
        private String email;
    }




}
