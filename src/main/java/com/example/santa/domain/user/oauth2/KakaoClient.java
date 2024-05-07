package com.example.santa.domain.user.oauth2;

import com.example.santa.domain.user.entity.SocialType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

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
        
        // 바디
        MultiValueMap<String, String> body = oauthParams.makeBody();
        body.add("grant_type", GRANT_TYPE);
        body.add("client_id", clientId);
        body.add("redirect_uri", redirectUri);

        // 헤더 + 바디
        HttpEntity<MultiValueMap<String, String>> tokenRequest = new HttpEntity(body, headers);
        // 토큰 수신
        KakaoToken kakaoToken = rt.postForObject(url, tokenRequest, KakaoToken.class);

        return kakaoToken.getAccessToken();
    }

    @Override
    public OauthUser getMemberInfo(String accessToken) {
        String url = userUrl;
        RestTemplate rt = new RestTemplate();
        // 헤더
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        headers.add("Authorization", "Bearer" + accessToken);
        // 바디
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.add("property_keys", "[\"kakao_account.email\"]");

        HttpEntity<MultiValueMap<String, String>> infoRequest = new HttpEntity<>(body, headers);

        return rt.postForObject(url, infoRequest, KakaoUser.class);
    }
}
