package com.example.santa.global.security.jwt;

import com.example.santa.global.exception.ExceptionCode;
import com.example.santa.global.exception.ServiceLogicException;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SecurityException;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Component
public class JwtTokenProvider {
    /*
    * Security 와 JWT 사용하여 인증과 권한을 부여하는 클래스
    * 토근 생성, 복호화, 검증 기능 구현
    * */
    private final Key key;

    // secret 값 가져와서 key 에 저장
    public JwtTokenProvider(@Value("${jwt.secret}") String secretKey) {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    /*
    * Authentication(인증) 객체를 기반으로 AccessToken, RefreshToken 생성
    * AccessToken: 인증된 사용자의 권한 정보와 만료시간을 담고있음
    * RefreshToken: AccessToken 의 갱신을 위해 사용 됨
    * */
    public JwtToken generateToken(Authentication authentication) {
        // 권한 가져오기
        String authorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority).collect(Collectors.joining(","));
        log.info("authorities {}", authorities);
        long now = (new Date()).getTime();

        // AccessToken 생성
        Date accessTokenExpiresIn = new Date(now + 86400000);
        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .claim("type", "access")
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // RefreshToken 생성
        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim("auth", authorities)
                .claim("type", "refresh")
                .setExpiration(new Date(now + 86400000))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();

        // JwtToken 에 넣기
        return JwtToken.builder()
                .grantType("Bearer")
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();
    }

    public String generateAccessTokenFromRefreshToken(String refreshToken) {
        if (validateToken(refreshToken)) {
            throw new IllegalArgumentException("refreshToken 으로만 접근할 수 있습니다.");
        }
        // RefreshToken 으로부터 권한 정보 추출
        // 토큰 복호화
        Claims claims = parseClaims(refreshToken);
        String username = claims.getSubject();
        String authorities = claims.get("auth").toString();

        long now = (new Date()).getTime();

        Date accessTokenExpiresIn = new Date(now + 86400000);
        String newAccessToken = Jwts.builder()
                .setSubject(username)
                .claim("auth", authorities)
                .setExpiration(accessTokenExpiresIn)
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
        return newAccessToken;
    }

    // Jwt 토큰을 복호화하여 토큰에 들어있는 정보를 꺼내는 메소드
    public Authentication getAuthentication(String accessToken) {
        // Jwt 토큰 복호화
        Claims claims = parseClaims(accessToken);

        if (claims.get("auth") == null) {
            throw new RuntimeException("권한 정보가 없는 토큰입니다");
        }

        // claims 에서 권한정보 가져오기
        Collection<? extends GrantedAuthority> authorities = Arrays.stream(claims.get("auth").toString().split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
//        // test
//        Collection<? extends GrantedAuthority> authorities = List.of("user").stream()
//                .map(SimpleGrantedAuthority::new)
//                .collect(Collectors.toList());

        // UserDetails 객체를 만들어서 Authentication return
//        UserDetails principal = new User(claims.getSubject(), "", authorities);
        return new UsernamePasswordAuthenticationToken(claims.getSubject(), "", authorities);
    }

    // 토큰정보 검증 메소드
    public Boolean validateToken(String token) {
        try {
            Jws<Claims> claimsJws = Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            String type = claimsJws.getBody().get("type").toString();
            return type.equals("access");
        } catch (SecurityException | MalformedJwtException e) {
            log.info("invalid JWT Token", e);
            throw new ServiceLogicException(ExceptionCode.INVALID_JWT_TOKEN);
        } catch (ExpiredJwtException e) {
            log.info("Expired JWT Token", e);
            throw new ServiceLogicException(ExceptionCode.EXPIRED_JWT_TOKEN);
        } catch (UnsupportedJwtException e) {
            log.info("Unsupported JWT Token", e);
            throw new ServiceLogicException(ExceptionCode.UNSUPPORTED_JWT_TOKEN);
//        } catch (IllegalArgumentException e) {
//            log.info("JWT claims string is empty", e);
        }
//        return false;
    }

    /*
    * Claims: 토큰에서 사용할 정보의 조각
    * accessToken 을 복호화 만약 만료된 토큰이면 Claims 반환
    * JWT 토큰의 검증과 파싱을 모두 수행한다.
    * */
    private Claims parseClaims(String accessToken) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(accessToken)
                    .getBody();
        } catch (ExpiredJwtException e) {
            return e.getClaims();
        }
    }


}
