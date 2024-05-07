package com.example.santa.domain.user.controller;

import com.example.santa.domain.user.oauth2.KakaoParams;
import com.example.santa.domain.user.oauth2.OAuthService;
import com.example.santa.global.security.jwt.JwtToken;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController("/oauth2")
@RequiredArgsConstructor
@Slf4j
public class OAuth2Controller {
    private final OAuthService oAuthService;
    @PostMapping("/kakao")
    public ResponseEntity<JwtToken> KakaoLogin(@RequestBody KakaoParams kakaoParams){
        log.debug("Kakao 인증키 {} ", kakaoParams.getAuthorizationCode());
        JwtToken jwtToken = oAuthService.getUserByOauthSignIn(kakaoParams);

        return ResponseEntity.status(HttpStatus.OK).body(jwtToken);
    }
}
