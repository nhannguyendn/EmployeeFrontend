package com.example.employee.employee.dto;

public class EmployeeDTO {
    private Long id;
    private String fistName;
    private String lastName;
    private String emailId;

    public EmployeeDTO() {

    }

    public EmployeeDTO(Long id, String firstName, String lastName, String emailId) {
        this.id = id;
        this.fistName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getFistName() {
        return fistName;
    }

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
