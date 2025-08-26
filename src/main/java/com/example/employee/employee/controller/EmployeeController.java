package com.example.employee.employee.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
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

import com.example.employee.employee.dto.EmployeeDTO;
import com.example.employee.employee.dto.PagedResponse;
import com.example.employee.employee.exception.ResoureNotFoundException;
import com.example.employee.employee.model.Employee;
import com.example.employee.employee.model.EmployeeCard;
import com.example.employee.employee.respository.CardRespository;
import com.example.employee.employee.respository.EmployeeRespository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeRespository employeeRespository;
    @Autowired
    private CardRespository cardRespository;

    /**
     * get employees
     * 
     * @return listEmployees
     */
    @GetMapping("/employees")
    public List<Employee> getAllEmployees() {
        logger.info("getAllEmployees");
        return employeeRespository.findAll();
    }

    /**
     * Create employee
     */
    @PostMapping("/employees")
    @Transactional
    public Employee createEmployee(@RequestBody Employee employee) {
        String newCardNumber = generateNextCardNumber();
        EmployeeCard card = new EmployeeCard();
        card.setCardNumber(newCardNumber);
        // card.setEmployee(employee);

        employee.setCard(card);

        return employeeRespository.save(employee);
    }

    /**
     * Get employee by id
     */
    @GetMapping("/employees/{employeeId}")
    @Transactional(readOnly = true)
    public ResponseEntity<Employee> getEmployeesById(@PathVariable Long employeeId) {
        return employeeRespository.findById(employeeId).map(employee -> ResponseEntity.ok(employee))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update employee with id
     */
    @PutMapping("/employees/{employeeId}")
    @Transactional
    public ResponseEntity<Employee> updateEmployees(@PathVariable Long employeeId,
            @RequestBody Employee employeeDetails) {
        Employee employee = employeeRespository.findById(employeeId)
                .orElseThrow(() -> new ResoureNotFoundException("Not found employee id=" + employeeId));

        employee.setEmailId(employeeDetails.getEmailId());
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        Employee employeeUpdated = employeeRespository.save(employee);
        // todo: Check table project, team, card to update employee
        return ResponseEntity.ok(employeeUpdated);
    }

    /**
     * Delete employeee with id
     */

    @DeleteMapping("/employees/{employeeId}")
    @Transactional
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long employeeId) {
        Map<String, Boolean> result = new HashMap<>();

        Optional<Employee> employeeOpt = employeeRespository.findById(employeeId);

        // Employee employee = employeeRespository.findById(employeeId)
        // .orElseThrow(() -> new ResoureNotFoundException("Not found employee id =" +
        // employeeId));

        if (employeeOpt.isPresent()) {
            employeeRespository.delete(employeeOpt.get());
            // Auto table project, team, card to delete employee
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

    /**
     * Search employees by name or email (LIKE %email%)
     */
    @GetMapping("/employees/keywork")
    public ResponseEntity<List<Employee>> searchKeywordEmployee(@RequestParam("keyword") String keyword) {
        List<Employee> employees = employeeRespository.searchEmployees(keyword);
        return ResponseEntity.ok(employees);
    }

    /**
     * Search employees by keyword (email or firstName) + pagination + sort
     */
    @GetMapping("/employees/search-keywork")
    public ResponseEntity<PagedResponse<Employee>> searchByKeywordPage(
            @RequestParam String keyword,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir) {

        Pageable pageable = PageRequest.of(
                page,
                size,
                sortDir.equalsIgnoreCase("asc")
                        ? Sort.by(sortBy).ascending()
                        : Sort.by(sortBy).descending());

        // public ResponseEntity<Page<Employee>> searchByKeywordPage(
        Page<Employee> employees = employeeRespository.searchByKeywordPageWithMetadata(keyword, pageable);
        // return ResponseEntity.ok(employees);
        // custom response
        return ResponseEntity.ok(PagedResponse.fromPage(employees));

        // List<Employee> employees = employeeRespository.searchByKeywordPage(keyword,
        // pageable);
        // return ResponseEntity.ok(employees);
    }

    /**
     * Search employees by firstName or lastName
     */
    @GetMapping("/employees/email")
    public ResponseEntity<List<Employee>> findByEmail(@RequestParam("email") String name) {
        List<Employee> employees = employeeRespository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
        return ResponseEntity.ok(employees);
    }

    /**
     * Search employees by firstName or lastName
     */
    @GetMapping("/employees/firstName")
    public ResponseEntity<List<Employee>> findByFirstName(@RequestParam("firstName") String name) {
        List<Employee> employees = employeeRespository
                .findByFirstName(name);
        return ResponseEntity.ok(employees);
    }

    private String generateNextCardNumber() {
        String prefix = "EMP";
        String maxCard = cardRespository.findMaxCardNumber();

        long next = 1L;
        if (maxCard != null && maxCard.startsWith(prefix)) {
            next = Long.parseLong(maxCard.substring(prefix.length())) + 1;
        }

        int minDigits = 4;
        int digits = Math.max(minDigits, String.valueOf(next).length());

        return prefix + String.format("%0" + digits + "d", next);
    }

}
