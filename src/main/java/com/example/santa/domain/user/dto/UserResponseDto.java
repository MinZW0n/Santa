package com.example.santa.domain.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserResponseDto {
    private Long id;

    private String email;

    private String nickname;

    private String name;

    private String phoneNumber;

    private String image;

    private double accumulatedHeight;
}
