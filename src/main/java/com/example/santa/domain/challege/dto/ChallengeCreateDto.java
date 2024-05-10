package com.example.santa.domain.challege.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;
import org.springframework.web.multipart.MultipartFile;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "챌린지 생성 요청 DTO")
public class ChallengeCreateDto {


    @Schema(description = "테스트 값", example = "1")
    private Long categoryId;

    @Schema(description = "테스트 값", example = "힐링 모임 5회 참여")
    @NotNull(message = "업적 명은 필수 입력란 입니다.")
    @Size(max = 20, message = "20자 이내로 작성해 주세요")
    private String name;

    @Schema(description = "테스트 값", example = "힐링 모임에 5회 참여해보세요!")
    @NotBlank(message = "업적 설명은 필수 입력란 입니다.")
    @Size(max = 1000, message = "10000자 이내로 작성해 주세요")
    private String description;

    @Schema(description = "테스트 값", example = "5")
    @NotNull(message = "완료 기준은 필수 입력란 입니다.")
    @Min(value = 1, message = "1이상의 값을 입력해 주세요.")
    private Integer clearStandard;

    private String image;

    private MultipartFile imageFile;
}
