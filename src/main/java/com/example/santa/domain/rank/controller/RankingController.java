package com.example.santa.domain.rank.controller;

import com.example.santa.domain.challege.dto.ChallengeResponseDto;
import com.example.santa.domain.rank.dto.RankingReponseDto;
import com.example.santa.domain.rank.entity.Ranking;
import com.example.santa.domain.rank.service.RankingService;
import com.example.santa.domain.rank.service.RankingServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/ranks")
public class RankingController {

    private final RankingService rankingService;

    @Operation(summary = "랭킹 조회 기능", description = "랭킹 조회 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ChallengeResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ChallengeResponseDto.class)))})
    @GetMapping
    public ResponseEntity<List<RankingReponseDto>> getAllRanksDto() {
        List<RankingReponseDto> ranking = rankingService.getRankingOrderedByScore();

        return ResponseEntity.ok(ranking);
    }

}
