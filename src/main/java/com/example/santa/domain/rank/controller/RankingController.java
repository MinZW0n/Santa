package com.example.santa.domain.rank.controller;

import com.example.santa.domain.challege.dto.ChallengeResponseDto;
import com.example.santa.domain.rank.dto.RankingResponseDto;
import com.example.santa.domain.rank.service.RankingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.nio.file.attribute.UserPrincipal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

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
    public ResponseEntity<List<RankingResponseDto>> getAllRanksDto() {
        List<RankingResponseDto> ranking = rankingService.getRankingOrderedByScore();

        return ResponseEntity.ok(ranking);
    }

    @Operation(summary = "랭킹 조회 기능(+사용자의 랭킹도 따로 보이도록)", description = "랭킹 조회 기능(+사용자의 랭킹도 따로 보이도록)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ChallengeResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ChallengeResponseDto.class)))})
    @GetMapping("/rankings")
    public ResponseEntity<Map<String, Object>> getAllRanksDto(@AuthenticationPrincipal String email) {
        List<RankingResponseDto> rankingList = rankingService.getRankingOrderedByScore();
        Optional<RankingResponseDto> userRanking = rankingService.getRankingByEmail(email);

        Map<String, Object> response = new HashMap<>();
        response.put("rankings", rankingList);
        userRanking.ifPresent(r -> response.put("userRanking", r));

        return ResponseEntity.ok(response);
    }
}
