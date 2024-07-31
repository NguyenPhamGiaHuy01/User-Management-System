package com.example.SpringSecurity_Angular.config;

import com.example.SpringSecurity_Angular.dto.ReqRes;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.core.OAuth2AuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OAuth2IntrospectionAuthenticatedPrincipal;
import org.springframework.security.oauth2.server.resource.introspection.OpaqueTokenIntrospector;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
public class GoogleOpaqueTokenIntrospector implements OpaqueTokenIntrospector {
    private final WebClient userInfoClient;

    @Override
    public OAuth2AuthenticatedPrincipal introspect(String token) {
        ReqRes userInfo = userInfoClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/oauth2/v3/userinfo")
                        .queryParam("access_token", token)
                        .build())
                .retrieve()
                .bodyToMono(ReqRes.class)
                .block();
        Map<String, Object> attributes = new HashMap<>();
        attributes.put("email", userInfo.getEmail());
        attributes.put("name", userInfo.getName());
        return new OAuth2IntrospectionAuthenticatedPrincipal(userInfo.getName(), attributes, null);
    }
}
