package com.example.santa.domain.user.dto;

import com.example.santa.domain.user.entity.SocialType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UserSignupRequestDto {
    @Schema(description = "테스트 값", example = "santa111@gmail.com")
    @Email(message = "이메일 양식이 올바르지 않습니다.")
    private String email;
    @Schema(description = "테스트 값", example = "1q2w3e4r!!")
    @Pattern(regexp = "^(?=.*[a-zA-Z])(?=.*[0-9])(?=.*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?]).{8,}[^\\s]*$", message = "비밀번호는 알파벳, 숫자, 특수문자를 포함하여 8자리 이상이어야 합니다.")
    @NotBlank
    private String password;
    @Schema(description = "테스트 값", example = "엘리스")
    @NotBlank
    private String name;
    @Schema(description = "테스트 값", example = "귀요미산악꾼")
    @Size(min = 2, max = 8, message = "닉네임은 최소 2자 최대 8자 까지 가능합니다.")
    @NotBlank
    private String nickname;
    @Schema(description = "테스트 값", example = "01023234545")
    @Pattern(regexp = "^01(?:0|1|[6-9])(?:\\d{3}|\\d{4})\\d{4}$", message = "휴대폰 번호 양식이 올바르지 않습니다.")
    @NotBlank
    private String phoneNumber;
    private SocialType socialType;
    private String socialId;
}
