package com.example.santa.domain.user.oauth;

import com.example.santa.domain.user.entity.Role;
import com.example.santa.domain.user.entity.SocialType;
import com.example.santa.domain.user.entity.User;
import com.example.santa.domain.user.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class OAuth2UserServiceImpl extends DefaultOAuth2UserService {
    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException {
        OAuth2User oAuth2User = super.loadUser(request);
        // 어떤 소셜에서 정보를 제공해주었는지 알기 위해서
        String registrationId = request.getClientRegistration().getRegistrationId();
        // OAuth2User 가지고 있는 전체정보에서 사용자 정보가 담긴 Key 를 의미
        String userNameAttributeName = request
                .getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();
        // 소셜타입 반환 enum
        SocialType socialType = OAuth2Utils.getSocialType(registrationId);
        // 소셜에서 전달받은 정보를 가진 OAuth2User 에서 Map 을 추출하여 OAuth2Attribute 를 생성
        Map<String, Object> attributes = oAuth2User.getAttributes();

        // 내부에서 OAuth2UserInfo 생성과 함께 OAuth2Attributes 를 생성해서 반환
        OAuth2Attributes oAuth2Attributes = OAuth2Attributes.of(socialType, userNameAttributeName, attributes);

        // User 생성을 위한 정보를 가지고 있는 OAuth2UserInfo
        OAuth2UserInfo oAuth2UserInfo = oAuth2Attributes.getOAuth2UserInfo();
        String socialId = oAuth2UserInfo.getSocialId();
        String email = oAuth2UserInfo.getEmail();

        // 소셜 타입과 소셜 ID 로 조회된다면 이전에 로그인을 한 유저
        // DB 에 조회되지 않는다면 Role 을 GUEST 로 설정하여 반환 -> 회원가입으로 리다이렉트 후 추가 정보를 받는다.
        User user = userRepository.findBySocialTypeAndSocialId(socialType, socialId)
                .orElse(User.builder().email(email).role(Role.GUEST).socialType(socialType).socialId(socialId).build());

        return new OAuth2UserImpl(Collections.singleton(new SimpleGrantedAuthority(user.getRole().toString())),
                attributes, oAuth2Attributes.getNameAttributeKey(), user);
    }
}
