package com.example.santa.global.security.jwt;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
@AllArgsConstructor
public class JwtToken {
    // 클라이언트에 토큰을 보내기 위한 DTO

    // JWT 인증타입: Bearer 사용
    // ex) Authorization: Bearer <access_token>
    private String grantType;
    private String accessToken;
    private String refreshToken;
}
