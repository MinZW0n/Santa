package com.example.santa.domain.mail.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class EmailCheckDto {
    @Schema(description = "테스트 값", example = "santa123@gmail.com")
    @Email
    @NotEmpty(message = "이메일을 입력해 주세요")
    private String email;
    @Schema(description = "테스트 값", example = "985032")
    @NotEmpty(message = "인증 번호를 입력해 주세요")
    private String authNumber;
}
