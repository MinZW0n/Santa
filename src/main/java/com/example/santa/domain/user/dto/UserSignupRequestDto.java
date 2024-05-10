package com.example.santa.domain.user.dto;

import com.example.santa.domain.user.entity.SocialType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserSignupRequestDto {
    @Schema(description = "테스트 값", example = "santa111@gmail.com")
    @Email
    private String email;
    @Schema(description = "테스트 값", example = "1q2w3e4r!!")
    @NotBlank
    private String password;
    @Schema(description = "테스트 값", example = "엘리스")
    @NotBlank
    private String name;
    @Schema(description = "테스트 값", example = "귀요미산악꾼")
    @NotBlank
    private String nickname;
    @Schema(description = "테스트 값", example = "01023234545")
    @NotBlank
    private String phoneNumber;
    private SocialType socialType;
    private String socialId;
}
