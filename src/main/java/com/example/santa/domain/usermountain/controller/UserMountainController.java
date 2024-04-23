package com.example.santa.domain.usermountain.controller;

import com.example.santa.domain.challege.dto.ChallengeResponseDto;
import com.example.santa.domain.mountain.entity.Mountain;
import com.example.santa.domain.usermountain.dto.UserMountainRequestDto;
import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import com.example.santa.domain.usermountain.dto.UserMountainVerifyRequestDto;
import com.example.santa.domain.usermountain.dto.UserMountainVerifyResponseDto;
import com.example.santa.domain.usermountain.entity.UserMountain;
import com.example.santa.domain.usermountain.service.UserMountainServiceImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/user-mountains")
public class UserMountainController {

    private final UserMountainServiceImpl userMountainServiceImpl;

    @Autowired
    public UserMountainController(UserMountainServiceImpl userMountainServiceImpl){
        this.userMountainServiceImpl =userMountainServiceImpl;
    }


    @Operation(summary = "유저 마운틴 등록기능", description = "유저 마운틴 등록")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation =ChallengeResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ChallengeResponseDto.class)))})
    @PostMapping
    public ResponseEntity<UserMountainResponseDto> createUserMountain(@RequestBody UserMountainVerifyRequestDto request) {
        UserMountainResponseDto userMountains = userMountainServiceImpl.verifyAndCreateUserMountain1(request);

//            return new ResponseEntity<>(userMountains, HttpStatus.CREATED);
        return ResponseEntity.ok(userMountains);
    }

    @Operation(summary = "유저 마운틴 조회 기능", description = "전체 유저 마운틴 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ChallengeResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ChallengeResponseDto.class)))})
    @GetMapping
    public ResponseEntity<List<UserMountainResponseDto>> getAllUserMountains() {
        List<UserMountainResponseDto> userMountains = userMountainServiceImpl.getAllUserMountains();
        return ResponseEntity.ok(userMountains);
    }

    @Operation(summary = "유저 마운틴 개별 조회 기능", description = "유저 마운틴 고유 id로 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = ChallengeResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = ChallengeResponseDto.class)))})
    @GetMapping("/{id}")
    public ResponseEntity<UserMountainResponseDto> getUserMountainById(@PathVariable Long id) {
        UserMountainResponseDto userMountainDTO = userMountainServiceImpl.getUserMountainById(id);
        return ResponseEntity.ok(userMountainDTO);
    }


}


//    @PostMapping
//    public ResponseEntity<UserMountainResponseDto> createUserMountain(@RequestBody UserMountainRequestDto request) {
//        UserMountainResponseDto userMountainResponseDto = userMountainServiceImpl.verify2(request.getUserId(), request.getMountainId(), request.getLatitude(), request.getLongitude());
//        return ResponseEntity.ok(userMountainResponseDto);
//    }


//    @PostMapping
//    public ResponseEntity<UserMountainResponseDto> createUserMountain(@RequestBody UserMountainVerifyRequestDto request) {
//        UserMountainResponseDto userMountains = userMountainServiceImpl.verifyAndCreateUserMountain1(
//                    request.getLatitude(),
//                    request.getLongitude(),
//                    request.getClimbDate(),
//                    request.getUserEmail(),
//                    request.getCategoryId()
//            );
////            return new ResponseEntity<>(userMountains, HttpStatus.CREATED);
//            return ResponseEntity.ok(userMountains);
//    }



//    @PostMapping
//    public ResponseEntity<?> verifyAndCreate(@RequestBody UserMountainResponseDto userMountainResponseDto) {
//        boolean isCreated = userMountainServiceImpl.verify1(userMountainResponseDto);
//        if (isCreated) {
//            return ResponseEntity.ok().body("위치 인증 완료(usermountain 생성)");
//        } else {
//            return ResponseEntity.badRequest().body("위치가 일치하지 않습니다.");
//        }
//    }

//    @PostMapping
//    public ResponseEntity<UserMountainResponseDto> createUserMountain(@RequestParam Long userId, @RequestParam Long mountainId, @RequestParam double latitude, @RequestParam double longitude) {
//        UserMountainResponseDto userMountainResponseDto = userMountainServiceImpl.verify2(userId, mountainId, latitude, longitude);
//        return ResponseEntity.ok(userMountainResponseDto);
//    }