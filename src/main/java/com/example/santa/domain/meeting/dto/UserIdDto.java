package com.example.santa.domain.meeting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserIdDto {
    @Schema(description = "테스트 값", example = "1")
    private Long userId;
}
