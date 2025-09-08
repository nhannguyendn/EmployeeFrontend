package com.example.employee.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.employee.employee.dto.LoginRequest;
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
        user = userRepository.save(user);

        var employee = Employee.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .emailId(request.getEmail())
                .build();

        return employeeService.createNewEmployee(employee);
    }

    public String login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getPassword()));
        var user = userRepository.findByEmail(request.getEmail());
        if (user == null) {
            return null;
        }

        return jwtService.generateToken(user);
    }

}
