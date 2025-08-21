package com.example.employee.employee.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.employee.exception.ResoureNotFoundException;
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

    /**
     * Get employee by id
     */
    @GetMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> getEmployeesById(@PathVariable Long employeeId) {
        return employeeRespository.findById(employeeId).map(employee -> ResponseEntity.ok(employee))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update employee with id
     */
    @PutMapping("/employees/{employeeId}")
    public ResponseEntity<Employee> updateEmployees(@PathVariable Long employeeId,
            @RequestBody Employee employeeDetails) {
        Employee employee = employeeRespository.findById(employeeId)
                .orElseThrow(() -> new ResoureNotFoundException("Not found employee id=" + employeeId));

        employee.setEmailId(employeeDetails.getEmailId());
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        Employee employeeUpdated = employeeRespository.save(employee);
        return ResponseEntity.ok(employeeUpdated);
    }

    /**
     * Delete employeee with id
     */

    @DeleteMapping("/employees/{employeeId}")
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long employeeId) {
        Map<String, Boolean> result = new HashMap<>();

        Optional<Employee> employeeOpt = employeeRespository.findById(employeeId);

        // Employee employee = employeeRespository.findById(employeeId)
        // .orElseThrow(() -> new ResoureNotFoundException("Not found employee id =" +
        // employeeId));

        if (employeeOpt.isPresent()) {
            employeeRespository.delete(employeeOpt.get());
            result.put("Deleted", true);
        } else {
            result.put("Deleted", false);
        }
        return ResponseEntity.ok(result);
    }

    /**
     * Search employees by firstName or lastName
     */
    @GetMapping("/employees/search")
    public ResponseEntity<List<Employee>> searchEmployees(@RequestParam("name") String name) {
        List<Employee> employees = employeeRespository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
        return ResponseEntity.ok(employees);
    }

    /**
     * Search employees by email (LIKE %email%)
     */
    @GetMapping("/employees/search-email")
    public ResponseEntity<List<Employee>> searchEmailEmployee(@RequestParam("emailId") String email) {
        List<Employee> employees = employeeRespository.findByEmailIdContainingIgnoreCase(email);
        return ResponseEntity.ok(employees);
    }

}
