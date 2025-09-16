package com.example.employee.employee.controller;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import com.example.employee.employee.model.LoginType;
import com.example.employee.security.oauth2.CustomOAuth2User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.employee.dto.LoginRequest;
import com.example.employee.employee.dto.LoginResponse;
import com.example.employee.employee.dto.RegisterRequest;
import com.example.employee.employee.model.Employee;
import com.example.employee.employee.model.User;
import com.example.employee.employee.repository.UserRepository;
import com.example.employee.security.service.AuthService;
import com.example.employee.security.service.CustomUserDetailsService;
import com.example.employee.security.service.JwtService;
import com.example.employee.utils.Constant;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.client.RestTemplate;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String googleClientId;

    @PostMapping("/register")
    @Transactional("employeeTransactionManager")
    public ResponseEntity<Employee> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/login")
    @Transactional("employeeTransactionManager")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/login-google")
    public ResponseEntity<LoginResponse> loginGoogle(@RequestBody Map<String, String> body) {
        String googleToken = body.get("googleToken");

        try {
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(new NetHttpTransport(), new JacksonFactory())
                    .setAudience(Collections.singletonList(googleClientId))
                    .build();

            GoogleIdToken idToken = verifier.verify(googleToken);
            if (idToken == null) {
                return ResponseEntity.ok(LoginResponse.builder()
                        .status(false)
                        .message("Invalid Google token")
                        .accessToken(null)
                        .build());
            }

            GoogleIdToken.Payload payload = idToken.getPayload();
            String email = payload.getEmail();
            String name = (String) payload.get("name");
            String firstName = (String) payload.get("given_name");
            String lastName = (String) payload.get("family_name");

            User user = customUserDetailsService.loadOrCreateUserFromGoogleLogin(email, name, firstName, lastName);
            String token = jwtService.generateToken(user);
            String refreshToken = jwtService.generateRefreshToken(user);
            return ResponseEntity.ok(LoginResponse.builder()
                    .status(token != null)
                    .accessToken(token)
                    .refreshToken(refreshToken)
                    .build());
        } catch (Exception e) {
            return ResponseEntity.ok(LoginResponse.builder()
                    .status(false)
                    .message("Google token verification failed")
                    .accessToken(null)
                    .build());
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request,
                                                      HttpServletResponse httpServletResponse) {
        String authHeader = request.getHeader("Authorization");
        Map<String, Object> response = new HashMap<>();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.put(Constant.KEY_RESULT, false);
            response.put(Constant.KEY_MESSAGE, "No token provided");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = authHeader.substring(7);

        try {

            if (jwtService.blacklistedTokens.contains(token)) {
                response.put(Constant.KEY_STATUS, false);
                response.put(Constant.KEY_MESSAGE, "Token already logged out");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            if (!jwtService.validateToken(token, userDetails)) {
                response.put(Constant.KEY_STATUS, false);
                response.put(Constant.KEY_MESSAGE, "Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            jwtService.blacklistToken(token);
            User user = userRepository.findByEmail(username);
            if (user != null && user.getLoginType() == LoginType.GOOGLE) {
                //revokeGoogleToken(user.getGoogleAccessToken());
            }

            // 3. Clear session (Spring Security session + cookie)
            request.getSession().invalidate();
            Cookie cookie = new Cookie("JSESSIONID", null);
            cookie.setMaxAge(0);
            cookie.setPath("/");
            httpServletResponse.addCookie(cookie);

            response.put(Constant.KEY_STATUS, true);
            response.put(Constant.KEY_MESSAGE, "Logged out successfully");
            response.put("token", null);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put(Constant.KEY_STATUS, false);
            response.put(Constant.KEY_MESSAGE, "Invalid token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    private void revokeGoogleToken(String accessToken) {
        try {
            String revokeUrl = "https://oauth2.googleapis.com/revoke";
            RestTemplate restTemplate = new RestTemplate();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

            MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
            params.add("token", accessToken);

            HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

            ResponseEntity<String> response = restTemplate.postForEntity(revokeUrl, request, String.class);
            System.out.println("Google revoke response: " + response.getStatusCode());

        } catch (Exception e) {
            System.err.println("Failed to revoke Google token: " + e.getMessage());
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get(Constant.KEY_REFRESH_TOKEN);

        Map<String, Object> response = new HashMap<>();
        if (refreshToken == null || !jwtService.validateRefreshToken(refreshToken)) {
            response.put(Constant.KEY_STATUS, false);
            response.put(Constant.KEY_MESSAGE, "Invalid or expired refresh token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(username);

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user);
        //jwtService.blacklistToken(token); block old access token if need

        response.put(Constant.KEY_STATUS, true);
        response.put(Constant.KEY_ACCESS_TOEKN, newAccessToken);
        response.put(Constant.KEY_REFRESH_TOKEN, newRefreshToken);
        return ResponseEntity.ok(response);
    }

}
