package com.example.employee.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.employee.employee.dto.LoginRequest;
import com.example.employee.employee.dto.LoginResponse;
import com.example.employee.employee.dto.RegisterRequest;
import com.example.employee.employee.model.Employee;
import com.example.employee.employee.model.Role;
import com.example.employee.employee.model.User;
import com.example.employee.employee.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtService jwtService;

    private final AuthenticationManager authenticationManager;
    @Autowired
    private UserRepository userRepository;
    private final EmployeeService employeeService;

    private final PasswordEncoder passwordEncoder;

    public Employee register(RegisterRequest request) {

        if (!request.getEmail().matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            return null;
        }

        if (userRepository.existsByEmail(request.getEmail())) {
            return null;
        }

        var user = User.builder()
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .build();
        userRepository.save(user);

        var employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .emailId(request.getEmail())
                .build();

        return employeeService.createNewEmployee(employee);
    }

    public LoginResponse login(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            return LoginResponse.builder()
                    .status(false)
                    .accessToken(null)
                    .build();
        }

        var user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return LoginResponse.builder()
                    .status(false)
                    .accessToken(null)
                    .build();
        }

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return LoginResponse.builder()
                .status(token != null)
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }

    public LoginResponse refreshToken(LoginRequest request) {
        try {
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        } catch (AuthenticationException e) {
            return LoginResponse.builder()
                    .status(false)
                    .build();
        }

        var user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return LoginResponse.builder()
                    .status(false)
                    .build();
        }

        String token = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);
        return LoginResponse.builder()
                .status(token != null)
                .accessToken(token)
                .refreshToken(refreshToken)
                .build();
    }
}
