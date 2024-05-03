package com.example.santa.domain.user.service;

import com.example.santa.domain.preferredcategory.dto.PreferredCategoryRequestDto;
import com.example.santa.domain.preferredcategory.dto.PreferredCategoryResponseDto;
import com.example.santa.domain.rank.dto.RankingResponseDto;
import com.example.santa.domain.user.dto.UserResponseDto;
import com.example.santa.domain.user.dto.UserSignInRequestDto;
import com.example.santa.domain.user.dto.UserSignupRequestDto;
import com.example.santa.domain.user.dto.UserUpdateRequestDto;
import com.example.santa.domain.userchallenge.dto.UserChallengeCompletionResponseDto;
import com.example.santa.domain.usermountain.dto.UserMountainResponseDto;
import com.example.santa.global.security.jwt.JwtToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    // create
    Long signup(UserSignupRequestDto request);
    // duplicate
    Boolean checkEmailDuplicate(String email);
    Boolean checkNicknameDuplicate(String nickname);
    // login
    JwtToken signIn(UserSignInRequestDto userSignInRequestDto);

    // refreshToken 으로 accessToken 재발급
    String generateAccessToken(String refreshToken);
    // read
    UserResponseDto findUserByEmail(String email);

    // Users read(관리자)
    Page<UserResponseDto> findAllUser(Pageable pageable);

    UserResponseDto updateUser(String email, UserUpdateRequestDto userUpdateRequestDto);

    String changePassword(String email, String oldPassword, String newPassword);

    String resetPassword(String email, String newPassword);
//    Page<UserMountainResponseDto> findAllUserMountains(Pageable pageable);

    Page<UserMountainResponseDto> findAllUserMountains(String email, Pageable pageable);

    Page<UserChallengeCompletionResponseDto> findChallengesByCompletion(String email, boolean completion, Pageable pageable);

    //개인 랭킹 조회
    RankingResponseDto getIndividualRanking(String email);

    // 선호카테고리 생성
    Long savePreferredCategory(String email, PreferredCategoryRequestDto preferredCategoryRequestDto);

    List<Long> savePreferredCategories(String email, List<Long> categoryIds);
    // 선호카테고리 전체조회
    List<PreferredCategoryResponseDto> findAllPreferredCategories(String email);

    // 선호카테고리 전체삭제
    void deleteAllPreferredCategory(String email);
}
