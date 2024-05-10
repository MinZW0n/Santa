package com.example.santa.domain.report.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ReportResponseDto {
    @Schema(description = "테스트 값", example = "1")
    private Long id;
    @Schema(description = "테스트 값", example = "욕설")
    private String reason;
    @Schema(description = "테스트 값", example = "나정균")
    private String reportedParticipantName;
    @Schema(description = "테스트 값", example = "정균")
    private String reportedParticipantNickname;
    @Schema(description = "테스트 값", example = "9")
    private Long reportedParticipantId;
    @Schema(description = "테스트 값", example = "옥찬혁")
    private String reporterName;
    @Schema(description = "테스트 값", example = "찬혁")
    private String reporterNickname;
    @Schema(description = "테스트 값", example = "10")
    private Long reporterId;
}
