package com.example.santa.domain.challege.controller;

import com.example.santa.domain.challege.dto.ChallengeCreateDto;
import com.example.santa.domain.challege.dto.ChallengeResponseDto;
import com.example.santa.domain.challege.entity.Challenge;
import com.example.santa.domain.challege.service.ChallengeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController {


    private final ChallengeService challengeService;


    @Operation(summary = "챌린지 조회 기능", description = "전체 챌린지 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation =ChallengeResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ChallengeResponseDto.class)))})
    @GetMapping
    public ResponseEntity<Page<ChallengeResponseDto>> getAllChallenges(@RequestParam(defaultValue = "0") int page,
                                                                       @RequestParam(defaultValue = "10") int size){

        Page<ChallengeResponseDto> challenges = challengeService.findAllChallenges(PageRequest.of(page, size, Sort.by("createdDate").descending()));
        return ResponseEntity.ok(challenges);

    }

    //챌린지 ID로 조회
    @Operation(summary = "챌린지 조회 기능", description = "챌린지 고유 id로 검색")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation =ChallengeResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ChallengeResponseDto.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<ChallengeResponseDto> getChallengeById(@PathVariable(name = "id") Long id) {
        ChallengeResponseDto challengeById = challengeService.findChallengeById(id);
        return ResponseEntity.ok(challengeById);
    }


    // 챌린지 등록
    @Operation(summary = "*관리자* 챌린지 등록기능", description = "챌린지 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation =ChallengeResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ChallengeResponseDto.class)))})
    @PostMapping
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ChallengeResponseDto> createChallenge(@ModelAttribute @Valid ChallengeCreateDto challengeCreateDto) {
        ChallengeResponseDto savedChallenge = challengeService.saveChallenge(challengeCreateDto);
        return ResponseEntity.ok(savedChallenge);
    }

    @Operation(summary = "*관리자* 등록 챌린지 수정 기능", description = "등록된 챌린지 정보 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation =ChallengeResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ChallengeResponseDto.class)))})
    @PatchMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<ChallengeResponseDto> updateChallenge(@PathVariable(name = "id") Long id, @RequestBody @Valid ChallengeCreateDto challengeCreateDto) {
        ChallengeResponseDto updatedChallenge = challengeService.updateChallenge(id, challengeCreateDto);
        return ResponseEntity.ok(updatedChallenge);
    }

    @Operation(summary = "*관리자* 등록 챌린지 삭제 기능", description = "등록된 챌린지를 챌린지 고유 id로 삭제")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = void.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = void.class)))})
    @DeleteMapping("/{id}")
    @PreAuthorize("hasAuthority('ADMIN')")
    public ResponseEntity<Void> deleteChallenge(@PathVariable(name = "id") Long id) {
        challengeService.deleteChallenge(id);
        return ResponseEntity.noContent().build(); // 204
    }
}


//    // 모든 챌린지 조회
//    @Operation(summary = "챌린지 조회 기능", description = "전체 챌린지 조회")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation =ChallengeResponseDto.class))),
//            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ChallengeResponseDto.class)))})
////    @GetMapping
////    public ResponseEntity<List<Challenge>> getAllChallenges() {
////        List<Challenge> challenges = challengeService.findAllChallenges();
////        return ResponseEntity.ok(challenges);
////    }
//    @GetMapping
//    public ResponseEntity<List<ChallengeResponseDto>> getAllChallenges() {
//        List<ChallengeResponseDto> challengeDtos = challengeService.findAllChallenges();
//        return ResponseEntity.ok(challengeDtos);
//    }