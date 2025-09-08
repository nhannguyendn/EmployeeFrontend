package com.example.employee.employee.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.transaction.annotation.Transactional;
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

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final CustomUserDetailsService customUserDetailsService;
    private final UserRepository userRepository;

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

    @PostMapping("/logout")
    public ResponseEntity<Map<String, Object>> logout(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        Map<String, Object> response = new HashMap<>();

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            response.put("status", false);
            response.put("message", "No token provided");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String token = authHeader.substring(7);

        try {

            if (jwtService.blacklistedTokens.contains(token)) {
                response.put("status", false);
                response.put("message", "Token already logged out");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
            String username = jwtService.extractUsername(token);
            UserDetails userDetails = customUserDetailsService.loadUserByUsername(username);

            if (!jwtService.validateToken(token, userDetails)) {
                response.put("status", false);
                response.put("message", "Invalid or expired token");
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }

            jwtService.blacklistToken(token);

            response.put("status", true);
            response.put("message", "Logged out successfully");
            response.put("token", null);
            return ResponseEntity.ok(response);

        } catch (Exception e) {
            response.put("status", false);
            response.put("message", "Invalid token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @PostMapping("/refresh-token")
    public ResponseEntity<Map<String, Object>> refreshToken(@RequestBody Map<String, String> request) {
        String refreshToken = request.get("refreshToken");

        Map<String, Object> response = new HashMap<>();
        if (refreshToken == null || !jwtService.validateRefreshToken(refreshToken)) {
            response.put("status", false);
            response.put("message", "Invalid or expired refresh token");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }

        String username = jwtService.extractUsername(refreshToken);
        User user = userRepository.findByEmail(username);

        String newAccessToken = jwtService.generateToken(user);
        String newRefreshToken = jwtService.generateRefreshToken(user); 
        //jwtService.blacklistToken(token); block old access token if need

        response.put("status", true);
        response.put("accessToken", newAccessToken);
        response.put("refreshToken", newRefreshToken);
        return ResponseEntity.ok(response);
    }

}
