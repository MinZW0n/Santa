package com.example.santa.global.security.jwt;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import java.io.IOException;

@RequiredArgsConstructor
public class JwtAuthenticationFilter extends GenericFilter {

    /*
    * 클라리언트 요청시 JWT 인증을 하기 위해 설치하는 커스텀 필터
    * 클라이언트 요청 -> JWT 토큰 처리 -> 유요한 토큰인 경우 Authentication 을 SecurityContext 에 저장
    * UsernamePasswordAuthenticationFilter 이전에 실행
    * */
    private final JwtTokenProvider jwtTokenProvider;

    /*
    * resolveToken() 사용하여 요청 헤더에서 JWT 토큰을 추출
    * validateToken() 메서드로 JWT 토큰의 유효성 검증
    * */
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain filterChain) throws IOException, ServletException {
        String token = resolveToken((HttpServletRequest) request);

        // 검증하고 유효하다면 SecurityContext 에 저장
        if (token != null) {
            if (jwtTokenProvider.validateToken(token)) {
                Authentication authentication = jwtTokenProvider.getAuthentication(token);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            else {
                HttpServletResponse httpServletResponse = (HttpServletResponse) response;
                httpServletResponse.sendError(403, "잘못된 토큰입니다");
            }
        }
        filterChain.doFilter(request, response);
    }

    // request 토큰 정보 추출 Authorization 헤더에서 "Bearer" 접두사로 시작하는 토큰 추출하여 반환
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
