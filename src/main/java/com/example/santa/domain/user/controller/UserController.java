package com.example.santa.domain.user.controller;

import com.example.santa.domain.user.dto.PasswordChangeRequestDto;
import com.example.santa.domain.user.dto.UserResponseDto;
import com.example.santa.domain.user.dto.UserSignupRequestDto;
import com.example.santa.domain.user.dto.UserUpdateRequestDto;
import com.example.santa.domain.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = Long.class)))})
    public ResponseEntity<Long> signup(@RequestBody @Valid UserSignupRequestDto request) {
        Long signup = userService.signup(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(signup);
    }

    @PostMapping("/duplicate/email")
    @Operation(summary = "이메일 중복 확인", description = "이메일 중복 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = Boolean.class)))})
    public ResponseEntity<Boolean> checkEmailDuplicate(@RequestBody String email) {
        Boolean checked = userService.checkEmailDuplicate(email);
        return ResponseEntity.status(HttpStatus.OK).body(checked);
    }

    @PostMapping("/duplicate/nickname")
    @Operation(summary = "닉네임 중복 확인", description = "닉네임 중복 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = Boolean.class)))})
    public ResponseEntity<Boolean> checkNicknameDuplicate(@RequestBody String nickname) {
        Boolean checked = userService.checkNicknameDuplicate(nickname);
        return ResponseEntity.status(HttpStatus.OK).body(checked);
    }

    @GetMapping("/{id}")
    @Operation(summary = "마이페이지", description = "마이페이지")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = UserResponseDto.class)))})
    public ResponseEntity<UserResponseDto> findUser(@PathVariable(name = "id") Long id) {
        UserResponseDto userById = userService.findUserById(id);
        return ResponseEntity.status(HttpStatus.OK).body(userById);
    }

    @GetMapping("")
    @Operation(summary = "회원조회", description = "회원조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = UserResponseDto.class)))})
    public ResponseEntity<Page<UserResponseDto>> findAllUser(@RequestParam(name = "size", defaultValue = "5") Integer size
            , @RequestParam(name = "page", defaultValue = "0") Integer page) {
        Page<UserResponseDto> allUser = userService.findAllUser(PageRequest.of(page, size));
        return ResponseEntity.status(HttpStatus.OK).body(allUser);
    }

    @PatchMapping("/{id}")
    @Operation(summary = "마이페이지 수정", description = "마이페이지 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = UserResponseDto.class)))})
    public ResponseEntity<UserResponseDto> updateUser(@PathVariable(name = "id") Long id, @RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto) {
        UserResponseDto updateUser = userService.updateUser(id, userUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateUser);
    }

    @PatchMapping("/password/{id}")
    @Operation(summary = "비밀번호 수정", description = "비밀번호 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = Long.class)))})
    public ResponseEntity<Long> changePassword(@PathVariable(name = "id") Long id, @RequestBody @Valid PasswordChangeRequestDto passwordChangeRequestDto) {
        Long changePassword = userService.changePassword(id
                , passwordChangeRequestDto.getOldPassword(), passwordChangeRequestDto.getNewPassword());
        return ResponseEntity.status(HttpStatus.OK).body(changePassword);
    }


}
