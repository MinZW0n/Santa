package com.example.santa.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordResetRequestDto {
    @Email
    @NotEmpty(message = "이메일을 입력해 주세요")
    private String email;
    @NotBlank
    private String newPassword;
}
