package com.example.santa.domain.user.controller;

import com.example.santa.domain.mail.dto.EmailCheckDto;
import com.example.santa.domain.mail.dto.EmailRequestDto;
import com.example.santa.domain.mail.service.EmailSendService;
import com.example.santa.domain.user.dto.*;
import com.example.santa.domain.user.service.UserService;
import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import com.example.santa.global.security.jwt.JwtToken;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;

@RequestMapping("/api/users")
@RestController
@RequiredArgsConstructor
@Slf4j
public class UserController {
    private final UserService userService;
    private final EmailSendService emailSendService;

    @PostMapping("/signup")
    @Operation(summary = "회원가입", description = "회원가입")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = Long.class)))})
    public ResponseEntity<Long> signup(@RequestBody @Valid UserSignupRequestDto userSignupRequestDto) {
        Long signup = userService.signup(userSignupRequestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(signup);
    }

    @PostMapping("/send-email")
    @Operation(summary = "이메일 인증코드 보내기", description = "이메일 인증코드 보내기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public String sendEmail(@RequestBody @Valid EmailRequestDto emailRequestDto) throws MessagingException, UnsupportedEncodingException {
        log.info("이메일 인증 이메일: {}", emailRequestDto.getEmail());
        String code = emailSendService.sendSimpleMessage(emailRequestDto.getEmail());
        log.info("인증코드: {}", code);
        return code;
    }

    @PostMapping("/verify-email")
    @Operation(summary = "이메일 인증", description = "이메일 인증")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public Boolean verifyEmail(@RequestBody @Valid EmailCheckDto emailCheckDto) {
        Boolean verifyEmail = emailSendService.verifyEmail(emailCheckDto.getAuthNumber(), emailCheckDto.getEmail());
        return verifyEmail;
    }


    @PostMapping("/sign-in")
    @Operation(summary = "로그인", description = "로그인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = JwtToken.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = JwtToken.class)))})
    public ResponseEntity<JwtToken> signIn(@RequestBody @Valid UserSignInRequestDto userSignInRequestDto) {
        JwtToken jwtToken = userService.signIn(userSignInRequestDto);
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(jwtToken);
    }

    @GetMapping("/check-token")
    public String checkToken(@AuthenticationPrincipal UserDetails userDetails) {
        return userDetails.getUsername();
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

    @GetMapping("/my")
    @Operation(summary = "마이페이지", description = "마이페이지")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = UserResponseDto.class)))})
    public ResponseEntity<UserResponseDto> findUser(@AuthenticationPrincipal String email) {
        UserResponseDto userByEmail = userService.findUserByEmail(email);
        return ResponseEntity.status(HttpStatus.OK).body(userByEmail);
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

    @PatchMapping("/password")
    @Operation(summary = "비밀번호 수정", description = "비밀번호 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = Long.class)))})
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal String email, @RequestBody @Valid PasswordChangeRequestDto passwordChangeRequestDto) {
        String changePassword = userService.changePassword(email
                , passwordChangeRequestDto.getOldPassword(), passwordChangeRequestDto.getNewPassword());
        return ResponseEntity.status(HttpStatus.OK).body(changePassword);
    }

    // 비밀번호 찾기 시 인증 이메일 보내고 인증완료 시 새로운 비밀번호 입력
//    @PostMapping("/password")
//    @Operation(summary = "비밀번호 찾기", description = "비밀번호 칮기")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
//            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = Long.class)))})
//    public ResponseEntity<String> findPassword(String email, String newPassword) {
//
//    }

    //유저 마운틴 전체조회
    @GetMapping("/mountains")
    @Operation(summary = "등반한 산 전체조회", description = "등반한 산 전체조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = UserResponseDto.class)))})
    public ResponseEntity<Page<UserMountainResponseDto>> getAllUserMountains(@AuthenticationPrincipal String email
            , @RequestParam(name = "size", defaultValue = "5") Integer size
            , @RequestParam(name = "page", defaultValue = "0") Integer page) {
        Page<UserMountainResponseDto> allUserMountains = userService.findAllUserMountains(email, PageRequest.of(page, size));
        return ResponseEntity.ok(allUserMountains);
    }



}
