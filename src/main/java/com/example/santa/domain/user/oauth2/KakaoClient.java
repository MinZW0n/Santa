package com.example.santa.domain.user.oauth2;

import com.example.santa.domain.user.entity.SocialType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

@Slf4j
@Component
public class KakaoClient implements OauthClient {
    private static final String GRANT_TYPE = "authorization_code";

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String tokenUrl;
    @Value("${spring.security.oauth2.client.provider.kakao.user-info-uri}")
    private String userUrl;
    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String redirectUri;
    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String clientSecret;

    @Override
    public SocialType socialType() {
        return SocialType.KAKAO;
    }

    @Override
    public String getOauthLonginToken(OauthParams oauthParams) {
        String url = tokenUrl;
        RestTemplate rt = new RestTemplate();
        // 해더
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Accept", "application/json");
        
        // 바디
        MultiValueMap<String, String> body = oauthParams.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("client_secret", clientSecret);
        body.add("redirect_uri", redirectUri);
        body.add("code", oauthParams.getAuthorizationCode());

        // 헤더 + 바디
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity(body, headers);
        // 토큰 수신
        KakaoToken kakaoToken = rt.postForObject(url, tokenRequest, KakaoToken.class);
//        ResponseEntity<KakaoToken> kakaoToken = rt.exchange(url, HttpMethod.POST, tokenRequest, KakaoToken.class);

        return kakaoToken.getAccessToken();
//        if (kakaoToken.getStatusCode() == HttpStatus.OK) {
//            return kakaoToken.getBody().getAccessToken();
//        } else {
//            throw new RuntimeException("Failed to retrieve access token");
//        }
    }

    @Override
    public OauthUser getMemberInfo(String accessToken) {
        log.info("accessToken {}", accessToken);

        String url = userUrl;
        RestTemplate rt = new RestTemplate();
        // 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.add("Authorization", "Bearer " + accessToken);
        // 바디
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\"]");

        HttpEntity<MultiValueMap<String, String>> infoRequest = new HttpEntity<>(body, headers);

        return rt.postForObject(url, infoRequest, KakaoUser.class);
    }
}
