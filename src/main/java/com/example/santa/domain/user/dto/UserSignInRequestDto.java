package com.example.santa.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserSignInRequestDto {
    @Schema(description = "테스트 값", example = "santa111@emai.com")
    @Email
    private String email;
    @Schema(description = "테스트 값", example = "1q2w3e4r!!")
    @NotNull
    private String password;
}
