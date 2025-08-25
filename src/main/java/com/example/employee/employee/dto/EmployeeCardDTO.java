package com.example.employee.employee.dto;

import com.example.employee.employee.model.Employee;

public class EmployeeCardDTO {
    private Long cardId;
    private String cardNumber;
    private EmployeeDTO employee;

    public EmployeeCardDTO() {

    }

    public EmployeeCardDTO(Long cardId, String cardNumber, Employee employee) {
        this.cardId = cardId;
        this.cardNumber = cardNumber;
        if (employee != null) {
            this.employee = new EmployeeDTO(employee.getId(), employee.getFirstName(), employee.getLastName(),
                    employee.getEmailId());
        }
    }

    public void setEmployee(EmployeeDTO employee) {
        this.employee = employee;
    }

    public EmployeeDTO getEmployee() {
        return employee;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public Long getCardId() {
        return cardId;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardId(Long cardId) {
        this.cardId = cardId;
    }
}
