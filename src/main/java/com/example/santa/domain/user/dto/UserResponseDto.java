package com.example.santa.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    @Schema(description = "테스트 값", example = "1")
    private Long id;
    @Schema(description = "테스트 값", example = "santa111@gmail.com")
    private String email;
    @Schema(description = "테스트 값", example = "귀요미산악꾼")
    private String nickname;
    @Schema(description = "테스트 값", example = "엘리스")
    private String name;
    @Schema(description = "테스트 값", example = "01023234545")
    private String phoneNumber;
    private String image;
    @Schema(description = "테스트 값", example = "1000")
    private double accumulatedHeight;
}
