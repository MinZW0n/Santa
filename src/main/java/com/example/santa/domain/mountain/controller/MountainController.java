package com.example.santa.domain.mountain.controller;

import com.example.santa.domain.mountain.service.MountainService;
import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import com.example.santa.domain.usermountain.dto.UserMountainVerifyRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/mountains")
public class MountainController {

//    @Autowired
//    private MountainService mountainService;
//    @Operation(summary = "유저 마운틴 등록 기능", description = "유저 마운틴 등록")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserMountainResponseDto.class))),
//            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = UserMountainResponseDto.class)))})
//    @PostMapping("/userMountain")
//    public ResponseEntity<UserMountainResponseDto> createUserMountain(@AuthenticationPrincipal String email, @RequestBody UserMountainVerifyRequestDto request) {
//        UserMountainResponseDto userMountain = mountainService.verifyAndCreateUserMountain(
//                request.getLatitude(), request.getLongitude(), request.getClimbDate(), email);
//        return ResponseEntity.ok(userMountain);
//    }

}
