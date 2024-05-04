package com.example.santa.domain.user.oauth;

import com.example.santa.domain.user.entity.Role;
import com.example.santa.domain.user.entity.User;
import com.example.santa.global.security.jwt.JwtToken;
import com.example.santa.global.security.jwt.JwtTokenProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class CustomOAuth2LoginSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;

    @Override
    public void onAuthenticationSuccess(
            HttpServletRequest request
            , HttpServletResponse response
            , Authentication authentication) throws IOException, ServletException {
        User user = ((OAuth2UserImpl) authentication.getPrincipal()).getUser();
        JwtToken jwtToken = jwtTokenProvider.generateToken(user);

        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(HttpServletResponse.SC_OK);

        String accessToken = jwtToken.getAccessToken();
        String refreshToken = jwtToken.getAccessToken();

        // 응답 바디에 추가 정보 입력
        Map<String, Object> data = new HashMap<>();

        if (user.getRole().equals(Role.GUEST)) {
            // 최초 로그인인 경우
            data.put("message", "회원가입을 완료해주세요.");
            data.put("redirectURL", "http://localhost:5173/join"); // 프론트엔드 회원가입 페이지 URL
        } else {
            // 최초 로그인이 아닌 경우
            data.put("message", "로그인 성공");
            data.put("redirectURL", "http://localhost:5173"); // 로그인 성공 후 리다이렉션할 프론트엔드 URL
        }

        // 공통 데이터
        data.put("accessToken", accessToken);
        data.put("refreshToken", refreshToken);
        data.put("email", user.getEmail());
        data.put("socialType", user.getSocialType().name());
        data.put("socialId", user.getSocialId());

        response.getWriter().write(objectMapper.writeValueAsString(data));
    }
}
