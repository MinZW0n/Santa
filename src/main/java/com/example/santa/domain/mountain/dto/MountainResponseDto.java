package com.example.santa.domain.mountain.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "산 정보 응답 DTO")
public class MountainResponseDto {
    @Schema(description = "테스트 값", example = "1")
    private Long Id;
    @Schema(description = "테스트 값", example = "가리산")
    private String name;
    @Schema(description = "테스트 값", example = "강원도 홍천군 두촌면ㆍ화촌면ㆍ 춘천시 북산면ㆍ동면")
    private String location;
    @Schema(description = "테스트 값", example = "1050.9")
    private double height;
    @Schema(description = "테스트 값", example = "강원도에서 진달래가 가장 많이 피는 산으로 알려져 있고... ")
    private String description;
    @Schema(description = "테스트 값", example = "가리산 자연휴양림 통나무 산막을 지나 ... ")
    private String point;
    @Schema(description = "테스트 값", example = "홍천에서 역내리ㆍ 천현리행 버스를 이용해 가리산자연휴양림에서 내린다.  ... ")
    private String transportation;
    @Schema(description = "테스트 값", example = "홍천에서 역내리ㆍ 천현리행 버스를 이용해 가리산자연휴양림에서 내린다.  ... ")
    private double longitude;
    private double latitude;
}
