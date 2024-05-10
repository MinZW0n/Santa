package com.example.santa.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class CheckEmailRequestDto {
    @Schema(description = "테스트 값", example = "santa123@gmail.com")
    @Email
    @NotEmpty(message = "이메일을 입력해 주세요")
    private String email;
}
