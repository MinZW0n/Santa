package com.example.santa.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequestDto {
    @NotBlank
    private String oldPassword;
    @NotBlank
    private String newPassword;
}
