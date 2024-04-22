package com.example.santa.domain.usermountain.controller;

import com.example.santa.domain.usermountain.dto.UserMountainRequestDto;
import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import com.example.santa.domain.usermountain.entity.UserMountain;
import com.example.santa.domain.usermountain.service.UserMountainServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/user-mountains")
public class UserMountainController {

    private final UserMountainServiceImpl userMountainServiceImpl;

    @Autowired
    public UserMountainController(UserMountainServiceImpl userMountainServiceImpl){
        this.userMountainServiceImpl =userMountainServiceImpl;
    }

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

    @PostMapping
    public ResponseEntity<UserMountainResponseDto> createUserMountain(@RequestBody UserMountainRequestDto request) {
        UserMountainResponseDto userMountainResponseDto = userMountainServiceImpl.verify2(request.getUserId(), request.getMountainId(), request.getLatitude(), request.getLongitude());
        return ResponseEntity.ok(userMountainResponseDto);
    }

    // GET endpoint for all UserMountains
    @GetMapping
    public ResponseEntity<List<UserMountainResponseDto>> getAllUserMountains() {
        List<UserMountainResponseDto> userMountains = userMountainServiceImpl.getAllUserMountains();
        return ResponseEntity.ok(userMountains);
    }

    // GET endpoint for a single UserMountain
    @GetMapping("/{id}")
    public ResponseEntity<UserMountainResponseDto> getUserMountainById(@PathVariable Long id) {
        UserMountainResponseDto userMountainDTO = userMountainServiceImpl.getUserMountainById(id);
        return ResponseEntity.ok(userMountainDTO);
    }


}
