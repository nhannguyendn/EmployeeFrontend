package com.example.employee.security.service;

import com.example.employee.employee.dto.EmployeeDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.employee.employee.model.Employee;
import com.example.employee.employee.model.EmployeeCard;
import com.example.employee.employee.repository.CardRepository;
import com.example.employee.employee.repository.EmployeeRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmployeeService {

    @Autowired
    private CardRepository cardRepository;
    @Autowired
    private EmployeeRepository employeeRepository;

     public Employee createNewEmployee(Employee employee) {
        String newCardNumber = generateNextCardNumber();
        EmployeeCard card = new EmployeeCard();
        card.setCardNumber(newCardNumber);
        // card.setEmployee(employee);

        employee.setCard(card);

        return employeeRepository.save(employee);
    }

    public Employee createNewEmployee(EmployeeDTO employeeDto) {
        String newCardNumber = generateNextCardNumber();
        EmployeeCard card = new EmployeeCard();
        card.setCardNumber(newCardNumber);
        // card.setEmployee(employee);

        Employee employee = new Employee();
        employee.setFirstName(employeeDto.getFirstName());
        employee.setLastName(employeeDto.getLastName());
        employee.setEmailId(employeeDto.getEmailId());
        employee.setCard(card);

        return employeeRepository.save(employee);
    }

    private String generateNextCardNumber() {
        String prefix = "EMP";
        String maxCard = cardRepository.findMaxCardNumber();

        long next = 1L;
        if (maxCard != null && maxCard.startsWith(prefix)) {
            next = Long.parseLong(maxCard.substring(prefix.length())) + 1;
        }

        int minDigits = 4;
        int digits = Math.max(minDigits, String.valueOf(next).length());

        return prefix + String.format("%0" + digits + "d", next);
    }
}
