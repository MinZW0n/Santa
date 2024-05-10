package com.example.santa.global.security.jwt;

import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "테스트 값", example = "Bearer")
    private String grantType;
    @Schema(description = "테스트 값", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0NThAZW1haWwuY29tIiwiYXV0aCI6IlVTRVIiLCJ0eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzE1MzIyOTkxfQ.187fvJ8zj3j7e_Ore3PZ_bc1r5Tr7r6hj1hk-ez6-Cg")
    private String accessToken;
    @Schema(description = "테스트 값", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0NThAZW1haWwuY29tIiwiYXV0aCI6IlVTRVIiLCJ0eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzE1MzIyOTkxfQsdfjklajdflkajdlfkamnvlkmdv123rkvkdhk-ez6-Cg")
    private String refreshToken;
    @Schema(description = "테스트 값", example = "USER")
    private String role;
}
