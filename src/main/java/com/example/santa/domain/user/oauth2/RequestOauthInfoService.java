package com.example.santa.domain.user.oauth2;

import com.example.santa.domain.user.entity.SocialType;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
public class RequestOauthInfoService {
    private final Map<SocialType, OauthClient> clientMap;

    public RequestOauthInfoService(List<OauthClient> clients) {
        this.clientMap = clients.stream().collect(
                Collectors.toUnmodifiableMap(OauthClient::socialType, Function.identity())
        );
    }

    public OauthUser request(OauthParams oauthParams) {
        OauthClient client = clientMap.get(oauthParams.socialType());
        String oauthLonginToken = client.getOauthLonginToken(oauthParams);
        return client.getMemberInfo(oauthLonginToken);
    }
}
