package com.example.santa.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PasswordChangeRequestDto {
    @Schema(description = "테스트 값", example = "a1s2d3f4@@")
    @NotBlank
    private String oldPassword;
    @Schema(description = "테스트 값", example = "1q2w3e4r!!")
    @NotBlank
    private String newPassword;
}
