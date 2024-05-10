package com.example.santa.domain.user.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserReportResponseDto {
    @Schema(description = "테스트 값", example = "1")
    private Long id;
    @Schema(description = "테스트 값", example = "santa111@gmail.com")
    private String email;
    @Schema(description = "테스트 값", example = "귀요미산악꾼")
    private String nickname;
    @Schema(description = "테스트 값", example = "엘리스")
    private String name;
    @Schema(description = "테스트 값", example = "4")
    private Long reportCount;

    public UserReportResponseDto(Long id, String email, String nickname, String name, Long reportCount) {
        this.id = id;
        this.email = email;
        this.nickname = nickname;
        this.name = name;
        this.reportCount = reportCount;
    }
}
