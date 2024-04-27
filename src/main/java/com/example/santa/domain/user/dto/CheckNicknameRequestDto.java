package com.example.santa.domain.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CheckNicknameRequestDto {
    @NotEmpty(message = "닉네임을 입력해 주세요")
    private String nickname;
}
