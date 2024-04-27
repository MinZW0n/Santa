package com.example.santa.domain.user.dto;

import com.example.santa.domain.user.entity.Password;
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
    @Email
    private String email;
    @NotNull
    private String password;
}
