package com.example.santa.domain.meeting.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class MeetingResponseDto {
    @Schema(description = "테스트 값", example = "1")
    private Long meetingId;
    @Schema(description = "테스트 값", example = "10")
    private Long leaderId;
    @Schema(description = "테스트 값", example = "산악회")
    @NotBlank(message = "모임 이름을 입력하세요.")
    private String meetingName;
    @Schema(description = "테스트 값", example = "등산")
    private String categoryName;
    @Schema(description = "테스트 값", example = "북한산")
    private String mountainName;
    @Schema(description = "테스트 값", example = "북한산 등산 후 식사")
    @NotBlank(message = "상세 설명을 입력하세요.")
    private String description;
    @Schema(description = "테스트 값", example = "15")
    @NotNull(message = "인원 수를 입력하세요.")
    private int headcount;
    @Schema(description = "테스트 값", example = "2024-05-20")
    @NotNull(message = "날짜를 입력하세요.")
    private LocalDate date;
    @Schema(description = "테스트 값", example = "산행, 등산모임")
    private List<String> tags;
    private String image;
    // 참가자 정보는 모임 상세 조회 시에만 사용
    private List<ParticipantDto> participants;




}
