package com.example.santa.domain.user.oauth2;

import com.example.santa.domain.user.entity.Role;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.example.santa.global.constant.Constants;
import com.example.santa.global.security.jwt.JwtToken;
import com.example.santa.global.security.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@PropertySource("classpath:application.properties")
@RequiredArgsConstructor
public class OAuthService {
    private final UserRepository userRepository;
    private final RequestOauthInfoService requestOauthInfoService;
    private final JwtTokenProvider jwtTokenProvider;

    public JwtToken getUserByOauthSignIn(OauthParams oauthParams) {
        OauthUser oauthUser = requestOauthInfoService.request(oauthParams);
        // 사용자가 존재하지 않는 경우 새로운 사용자를 생성하고 저장
        User user = userRepository.findByEmail(oauthUser.getEmail())
                .orElseGet(() -> {
                    User newUser = User.builder()
                            .email(oauthUser.getEmail())
                            .password(null) // OAuth 사용자이므로 비밀번호는 null
                            .nickname(null)
                            .phoneNumber(null)
                            .image(Constants.DEFAULT_URL + "user_default_image.png")
                            .role(Role.USER)
                            .socialType(oauthUser.socialType())
                            .socialId(null) // 필요하다면 OAuthUser에서 소셜 ID를 가져와 설정
                            .build();
                    return userRepository.save(newUser);
                });

        // 저장된 사용자 정보로 JWT 토큰 생성
        JwtToken jwtToken = jwtTokenProvider.generateToken(user);
        return jwtToken;
    }
}
