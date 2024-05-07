package com.example.santa.domain.user.oauth2;

import lombok.Data;

@Data
public class NaverToken {
    private String tokenType;
    private String accessToken;
    private String refreshToken;
    private int expiresIn;
}
