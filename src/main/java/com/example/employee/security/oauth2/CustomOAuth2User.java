package com.example.employee.security.oauth2;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Collection;
import java.util.Map;

public class CustomOAuth2User implements OAuth2User {

    private final OAuth2User oAuth2User;
    private final String accessToken;

    public CustomOAuth2User(OAuth2User oAuth2User, String accessToken) {
        this.oAuth2User = oAuth2User;
        this.accessToken = accessToken;
    }

    @Override
    public Map<String, Object> getAttributes() {
        return oAuth2User.getAttributes();
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return oAuth2User.getAuthorities();
    }

    @Override
    public String getName() {
        return oAuth2User.getName();
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getEmail() {
        return (String) oAuth2User.getAttributes().get("email");
    }

    public String getFirstName() {
        return (String) oAuth2User.getAttributes().get("given_name");
    }

    public String getLastName() {
        return (String) oAuth2User.getAttributes().get("family_name");
    }
}
