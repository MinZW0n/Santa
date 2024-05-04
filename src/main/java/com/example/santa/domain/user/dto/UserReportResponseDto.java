package com.example.santa.domain.user.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserReportResponseDto {
    private Long id;

    private String email;

    private String nickname;

    private String name;

    private Long reportCount;

    public UserReportResponseDto(Long id, String email, String nickname, String name, Long reportCount) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.name = name;
        this.reportCount = reportCount;
    }
}
