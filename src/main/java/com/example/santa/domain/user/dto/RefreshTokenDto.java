package com.example.santa.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class RefreshTokenDto {
    @Schema(description = "테스트 값", example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJ0ZXN0NThAZW1haWwuY29tIiwiYXV0aCI6IlVTRVIiLCJ0eXBlIjoiYWNjZXNzIiwiZXhwIjoxNzE1MzIyOTkxfQsdfjklajdflkajdlfkamnvlkmdv123rkvkdhk-ez6-Cg")
    private String refreshToken;
}
