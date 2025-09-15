package com.example.employee.security.handler;

import com.example.employee.employee.model.User;
import com.example.employee.employee.repository.UserRepository;
import com.example.employee.security.service.CustomUserDetailsService;
import com.example.employee.security.service.JwtService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.io.IOException;
import java.util.Map;

@Component
public class CustomOAuth2SuccessHandler implements AuthenticationSuccessHandler {

    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    @Autowired
    private UserRepository userRepository;

    public CustomOAuth2SuccessHandler(JwtService jwtService, CustomUserDetailsService customUserDetailsService) {
        this.jwtService = jwtService;
        this.customUserDetailsService = customUserDetailsService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2User oAuth2User = (OAuth2User) authentication.getPrincipal();
        Map<String, Object> attributes = oAuth2User.getAttributes();

        String email = (String) attributes.get("email");
        String fullName = (String) attributes.get("name");
        String firstName = (String) attributes.get("given_name");
        String lastName = (String) attributes.get("family_name");


        User user = customUserDetailsService.loadOrCreateUserFromGoogleLogin(email, fullName, firstName, lastName);

        String jwt = jwtService.generateToken(user);

        response.setContentType("application/json");
        //response.getWriter().write("{\"jwt token\": \"" + jwt + "\"}");
        response.getWriter().write("{\"message\": \"Login success\"}");
    }
}

