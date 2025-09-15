package com.example.employee.security.service;

import com.example.employee.employee.model.Employee;
import com.example.employee.employee.model.LoginType;
import com.example.employee.employee.model.Role;
import com.example.employee.employee.repository.EmployeeRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.employee.employee.model.User;
import com.example.employee.employee.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {
    private final UserRepository userRepository;
    private final EmployeeRepository employeeRepository;
    private final EmployeeService employeeService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(username);
        return org.springframework.security.core.userdetails.User.builder()
                .username(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().name())
                .build();
    }

    public User loadOrCreateUserFromGoogleLogin(String email, String name, String firstName, String lastName) {

        Employee employee = employeeRepository.findByEmail(email);
        if (employee == null) {
            var newEmployee = Employee.builder()
                    .firstName(firstName)
                    .lastName(lastName)
                    .emailId(email)
                    .build();
            employeeService.createNewEmployee(newEmployee);
        }

        User user = userRepository.findByEmail(email);
        if (user == null) {
            User newUser = User.builder()
                    .email(email)
                    .password("")
                    .role(Role.USER)
                    .loginType(LoginType.GOOGLE)
                    .build();
            return userRepository.save(newUser);
        }
        return user;
    }
}
