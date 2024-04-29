package com.example.santa.domain.user.controller;

import com.example.santa.domain.mail.dto.EmailCheckDto;
import com.example.santa.domain.mail.dto.EmailRequestDto;
import com.example.santa.domain.mail.service.EmailSendService;
import com.example.santa.domain.preferredcategory.dto.CategoriesRequestDto;
import com.example.santa.domain.preferredcategory.dto.PreferredCategoryRequestDto;
import com.example.santa.domain.preferredcategory.dto.PreferredCategoryResponseDto;
import com.example.santa.domain.user.dto.*;
import com.example.santa.domain.user.service.UserService;
import com.example.santa.domain.userchallenge.dto.UserChallengeCompletionResponseDto;
import com.example.santa.domain.userchallenge.entity.UserChallenge;
import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import com.example.santa.global.security.jwt.JwtToken;
import com.example.santa.global.util.mapsturct.UserChallengeCompletionResponseMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.mail.MessagingException;
import jakarta.transaction.Transactional;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
import java.util.List;

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
    @PostMapping("/duplicate/email")
    @Operation(summary = "이메일 중복 확인", description = "이메일 중복 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = Boolean.class)))})
    public ResponseEntity<Boolean> checkEmailDuplicate(@RequestBody @Valid CheckEmailRequestDto checkEmailRequestDto) {
        log.info("email {}", checkEmailRequestDto.getEmail());
        Boolean checked = userService.checkEmailDuplicate(checkEmailRequestDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(checked);
    }

    @PostMapping("/duplicate/nickname")
    @Operation(summary = "닉네임 중복 확인", description = "닉네임 중복 확인")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Boolean.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = Boolean.class)))})
    public ResponseEntity<Boolean> checkNicknameDuplicate(@RequestBody @Valid CheckNicknameRequestDto checkNicknameRequestDto) {
        Boolean checked = userService.checkNicknameDuplicate(checkNicknameRequestDto.getNickname());
        return ResponseEntity.status(HttpStatus.OK).body(checked);
    }

    @PostMapping("/send-email")
    @Operation(summary = "이메일 인증코드 보내기", description = "이메일 인증코드 보내기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<String> sendEmail(@RequestBody @Valid EmailRequestDto emailRequestDto) throws MessagingException, UnsupportedEncodingException {
        log.info("이메일 인증 이메일: {}", emailRequestDto.getEmail());
        String code = emailSendService.sendSimpleMessage(emailRequestDto.getEmail());
        log.info("인증코드: {}", code);
        return ResponseEntity.status(HttpStatus.OK).body(code);
    }

    @PostMapping("/verify-email")
    @Operation(summary = "이메일 인증", description = "이메일 인증")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<Boolean> verifyEmail(@RequestBody @Valid EmailCheckDto emailCheckDto) {
        Boolean verifyEmail = emailSendService.verifyEmail(emailCheckDto.getAuthNumber(), emailCheckDto.getEmail());
        return ResponseEntity.status(HttpStatus.OK).body(verifyEmail);
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

    @PostMapping("/new-access-token")
    @Operation(summary = "AccessToken 재발급", description = "AccessToken 재발급")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = Long.class)))})
    public ResponseEntity<String> generateAccessToken(@RequestBody String refreshToken) {
        String token = userService.generateAccessToken(refreshToken);
        return ResponseEntity.status(HttpStatus.CREATED).body(token);
    }

//    @GetMapping("/check-token")
//    public String checkToken(@AuthenticationPrincipal UserDetails userDetails) {
//        return userDetails.getUsername();
//    }

    @GetMapping("/my-info")
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

    @PatchMapping("/my-info")
    @Operation(summary = "마이페이지 수정", description = "마이페이지 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = UserResponseDto.class)))})
    public ResponseEntity<UserResponseDto> updateUser(@AuthenticationPrincipal String email, @RequestBody @Valid UserUpdateRequestDto userUpdateRequestDto) {
        UserResponseDto updateUser = userService.updateUser(email, userUpdateRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(updateUser);
    }

    @PatchMapping("/passwords")
    @Operation(summary = "비밀번호 수정", description = "비밀번호 수정")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<String> changePassword(@AuthenticationPrincipal String email, @RequestBody @Valid PasswordChangeRequestDto passwordChangeRequestDto) {
        String changePassword = userService.changePassword(email
                , passwordChangeRequestDto.getOldPassword(), passwordChangeRequestDto.getNewPassword());
        return ResponseEntity.status(HttpStatus.OK).body(changePassword);
    }

//     비밀번호 찾기 시 인증 이메일 보내고 인증완료 시 새로운 비밀번호 입력
    @PostMapping("/reset-passwords")
    @Operation(summary = "비밀번호 찾기", description = "비밀번호 칮기")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "성공", content = @Content(schema = @Schema(implementation = String.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = String.class)))})
    public ResponseEntity<String> resetPassword(@RequestBody @Valid PasswordResetRequestDto passwordResetRequestDto) {
        String resetPassword = userService.resetPassword(passwordResetRequestDto.getEmail(), passwordResetRequestDto.getNewPassword());
        return ResponseEntity.status(HttpStatus.OK).body(resetPassword);
    }

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

//    @PostMapping("/preferred-categories/test")
//    @Operation(summary = "선호카테고리 생성", description = "선호카테고리 생성")
//    @ApiResponses(value = {
//            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = Long.class))),
//            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = Long.class)))})
//    public ResponseEntity<Long> savePreferredCategory(@AuthenticationPrincipal String email, @RequestBody @Valid PreferredCategoryRequestDto preferredCategoryRequestDto) {
//        log.info("preferredCategoryRequestDto.getCategoryId() {}",preferredCategoryRequestDto.getCategoryId() );
//        log.info("email {}", email);
//        Long saved = userService.savePreferredCategory(email, preferredCategoryRequestDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
//    }

    @Transactional
    @PostMapping("/preferred-categories")
    @Operation(summary = "선호카테고리 생성", description = "선호카테고리 생성")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = List.class)))})
    public ResponseEntity<List<Long>> savePreferredCategory(@AuthenticationPrincipal String email, @RequestBody @Valid CategoriesRequestDto categoriesRequestDto) {
        // 지우고 생성
        userService.deleteAllPreferredCategory(email);
        List<Long> longList = userService.savePreferredCategories(email, categoriesRequestDto.getCategoryIds());
        return ResponseEntity.status(HttpStatus.CREATED).body(longList);
    }
    @GetMapping("/preferred-categories")
    @Operation(summary = "선호카테고리 전체조회", description = "선호카테고리 전체조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = List.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = List.class)))})
    public ResponseEntity<List<PreferredCategoryResponseDto>> findPreferredCategories(@AuthenticationPrincipal String email) {
        List<PreferredCategoryResponseDto> allPreferredCategories = userService.findAllPreferredCategories(email);
        return ResponseEntity.status(HttpStatus.OK).body(allPreferredCategories);
    }

    @GetMapping("/completion")
    @Operation(summary = "완료(true)/진행 중 업적(false) 조회", description = "완료(true)/진행 중 업적(false) 조회")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "성공", content = @Content(schema = @Schema(implementation = UserResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "에러", content = @Content(schema = @Schema(implementation = UserResponseDto.class)))})
    public ResponseEntity<Page<UserChallengeCompletionResponseDto>> getAllUserCompletions(@AuthenticationPrincipal String email
            , @RequestParam(name = "completion") boolean completion
            , @RequestParam(name = "size", defaultValue = "5") Integer size
            , @RequestParam(name = "page", defaultValue = "0") Integer page) {
        Page<UserChallengeCompletionResponseDto> allUserCompletions = userService.findChallengesByCompletion(email, completion, PageRequest.of(page, size));
        return ResponseEntity.ok(allUserCompletions);
    }


}
