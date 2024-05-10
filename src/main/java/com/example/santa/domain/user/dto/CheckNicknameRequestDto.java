package com.example.santa.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckNicknameRequestDto {
    @Schema(description = "테스트 값", example = "귀요미산악꾼")
    @NotEmpty(message = "닉네임을 입력해 주세요")
    private String nickname;
}
