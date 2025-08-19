package com.example.employee.employee.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.employee.model.Employee;
import com.example.employee.employee.respository.EmployeeRespository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    @Autowired
    private EmployeeRespository employeeRespository;

    /**
     * get employees
     * 
     * @return listEmployees
     */
    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        return employeeRespository.findAll();
    }

    /**
     * Create employee
     */

    @PostMapping("/employees")
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeRespository.save(employee);
    }
}
