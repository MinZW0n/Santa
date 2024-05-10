package com.example.santa.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequestDto {
    @Schema(description = "테스트 값", example = "santa111@email.com")
    @Email
    @NotEmpty(message = "이메일을 입력해 주세요")
    private String email;
    @Schema(description = "테스트 값", example = "1q2w3e4r!!")
    @NotBlank
    private String newPassword;
}
