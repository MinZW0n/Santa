package com.example.santa.domain.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportRequestDto {
    @NotBlank(message = "신고 사유를 입력하세요.")
    @Schema(description = "테스트 값", example = "욕설")
    private String reason;
    @Schema(description = "테스트 값", example = "9")
    private Long reportedParticipantId;
}
