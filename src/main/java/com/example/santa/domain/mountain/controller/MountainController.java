package com.example.santa.domain.mountain.controller;

import com.example.santa.domain.challege.dto.ChallengeResponseDto;
import com.example.santa.domain.mountain.dto.MountainResponseDto;
import com.example.santa.domain.mountain.service.MountainService;
import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import com.example.santa.domain.usermountain.dto.UserMountainVerifyRequestDto;
import com.example.santa.domain.usermountain.service.UserMountainService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/mountains")
public class MountainController {

    private final MountainService mountainService;
    private final UserMountainService userMountainService;

    @Operation(summary = "산 전체 조회 기능", description = "산 전체 조회 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MountainResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = MountainResponseDto.class)))})
    @GetMapping
    public ResponseEntity<Page<MountainResponseDto>> getAllMountains(@RequestParam(defaultValue = "0") int page,
                                                                     @RequestParam(defaultValue = "10") int size){
        Page<MountainResponseDto> mountains = mountainService.findAllMountains(PageRequest.of(page, size));
        return new ResponseEntity<>(mountains, HttpStatus.OK);
    }
    @Operation(summary = "산 개별 조회 기능", description = "산 개별 조회 기능")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = MountainResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = MountainResponseDto.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<MountainResponseDto> getMountainById(@PathVariable(name = "id") Long id){
        MountainResponseDto mountain = mountainService.findMountainById(id);
        return new ResponseEntity<>(mountain,HttpStatus.OK);
    }

    @Operation(summary = "등산 인증 기능(유저 마운틴 등록)", description = "등산 인증 기능(유저 마운틴 등록)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserMountainResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = UserMountainResponseDto.class)))})
    @PostMapping("/verify")
    public ResponseEntity<UserMountainResponseDto> createUserMountain(@AuthenticationPrincipal String email, @RequestBody UserMountainVerifyRequestDto request) {
        UserMountainResponseDto userMountain = userMountainService.verifyAndCreateUserMountain(
                request.getLatitude(), request.getLongitude(), request.getClimbDate(), email);
        return ResponseEntity.ok(userMountain);
    }

    @Operation(summary = "등산 인증 기능(유저 마운틴 등록)", description = "등산 인증 기능(유저 마운틴 등록)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공",
                    content = @Content(schema = @Schema(implementation = UserMountainResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = UserMountainResponseDto.class)))})
    @PostMapping("/test")
    public ResponseEntity<UserMountainResponseDto> createUserMountain1(@AuthenticationPrincipal String email, @RequestParam double latitude, double longitude, LocalDate climbDate) {

        System.out.println(email);
        UserMountainResponseDto userMountains = userMountainService.verifyAndCreateUserMountain(latitude,longitude,climbDate, email );

//            return new ResponseEntity<>(userMountains, HttpStatus.CREATED);
        return ResponseEntity.ok(userMountains);
    }

    @Operation(summary = "등산 인증 기능(유저 마운틴 등록)", description = "등산 인증 기능(유저 마운틴 등록)")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation =UserMountainResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = UserMountainResponseDto.class)))})
    @PostMapping("/test2")
    public ResponseEntity<UserMountainResponseDto> createUserMountain1(@AuthenticationPrincipal String email, @io.swagger.v3.oas.annotations.parameters.RequestBody UserMountainVerifyRequestDto request) {

        System.out.println(email);
        UserMountainResponseDto userMountains = userMountainService.verifyAndCreateUserMountain1(request, email);


//            return new ResponseEntity<>(userMountains, HttpStatus.CREATED);
        return ResponseEntity.ok(userMountains);
    }
}
