package com.example.employee.employee.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LoginResponse {
    private boolean status;
    private String token;
}
