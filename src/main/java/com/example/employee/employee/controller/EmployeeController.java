package com.example.employee.employee.controller;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import com.example.employee.attendance.model.Attendance;
import com.example.employee.attendance.repository.AttendanceRepository;
import com.example.employee.employee.dto.EmployeeDTO;
import com.example.employee.employee.dto.PagedResponse;
import com.example.employee.employee.model.Employee;
import com.example.employee.employee.repository.EmployeeRepository;
import com.example.employee.exception.ResoureNotFoundException;
import com.example.employee.salary.model.Salary;
import com.example.employee.salary.repository.SalaryRepository;
import com.example.employee.security.service.EmployeeService;

import lombok.RequiredArgsConstructor;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class EmployeeController {

    private static final Logger logger = LoggerFactory.getLogger(EmployeeController.class);

    @Autowired
    private EmployeeRepository employeeRepository;

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;

    private final EmployeeService employeeService;

    /**
     * get employees
     *
     * @return listEmployees
     */
    @GetMapping("/employees")
    //@PreAuthorize("hasRole('ADMIN')")
    //@Secured("ROLE_ADMIN")
    public List<Employee> getAllEmployees() {
        logger.info("getAllEmployees");
        return employeeRepository.findAll();
    }

    @GetMapping("/employee-pages")
    public ResponseEntity<PagedResponse<EmployeeDTO>> getAllEmployees(
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

        Page<Employee> employees = employeeRepository.findAll(pageable);
        List<Long> employeeIds = employees
                .stream()
                .map(Employee::getId)
                .toList();

        List<Attendance> attendances = attendanceRepository.findByEmployeeIdIn(employeeIds);
        Map<Long, List<Attendance>> attendanceMap = attendances.stream()
                .collect(Collectors.groupingBy(Attendance::getEmployeeId));

        List<Salary> salaries = salaryRepository.findByEmployeeIdIn(employeeIds);
        Map<Long, Salary> salariesMap = salaries.stream()
                .collect(Collectors.toMap(
                        Salary::getEmployeeId,
                        Function.identity(),
                        (existing, replacement) -> existing));

        Page<EmployeeDTO> dtoPage = employees.map(emp -> {
            EmployeeDTO dto = EmployeeDTO.fromEntity(emp);

            // salaryRepository.findByEmployeeId(emp.getId())
            // .ifPresent(salary -> dto.setSalary(salary));

            dto.setAttendances(attendanceMap.getOrDefault(emp.getId(), Collections.emptyList()));
            dto.setSalary(salariesMap.getOrDefault(emp.getId(), null));

            return dto;
        });

        return ResponseEntity.ok(PagedResponse.fromPage(dtoPage));
    }

    /**
     * Create employee
     */
    @PostMapping("/employees")
    @Transactional("employeeTransactionManager")
    public Employee createEmployee(@RequestBody Employee employee) {
        return employeeService.createNewEmployee(employee);
    }

    /**
     * Get employee by id
     */
    @GetMapping("/employees/{employeeId}")
    @Transactional(transactionManager = "employeeTransactionManager", readOnly = true)
    public ResponseEntity<Employee> getEmployeesById(@PathVariable Long employeeId) {
        return employeeRepository.findById(employeeId).map(employee -> ResponseEntity.ok(employee))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update employee with id
     */
    @PutMapping(value = "/employees/{employeeId}", consumes = {"application/json", "application/json;charset=UTF-8"})
    @Transactional("employeeTransactionManager")
    public ResponseEntity<Employee> updateEmployees(@PathVariable Long employeeId,
                                                    @RequestBody EmployeeDTO employeeDetails) {
        Employee employee = employeeRepository.findById(employeeId)
                .orElseThrow(() -> new ResoureNotFoundException("Not found employee id=" + employeeId));

        //employee.setEmailId(employeeDetails.getEmailId());
        employee.setFirstName(employeeDetails.getFirstName());
        employee.setLastName(employeeDetails.getLastName());
        Employee employeeUpdated = employeeRepository.save(employee);
        // todo: Check table project, team, card to update employee
        return ResponseEntity.ok(employeeUpdated);
    }

    /**
     * Delete employeee with id
     */

    @DeleteMapping("/employees/{employeeId}")
    @Transactional("employeeTransactionManager")
    public ResponseEntity<Map<String, Boolean>> deleteEmployee(@PathVariable Long employeeId) {
        Map<String, Boolean> result = new HashMap<>();

        Optional<Employee> employeeOpt = employeeRepository.findById(employeeId);

        // Employee employee = employeeRepository.findById(employeeId)
        // .orElseThrow(() -> new ResoureNotFoundException("Not found employee id =" +
        // employeeId));

        if (employeeOpt.isPresent()) {
            employeeRepository.delete(employeeOpt.get());
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
        List<Employee> employees = employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
        return ResponseEntity.ok(employees);
    }

    /**
     * Search employees by email (LIKE %email%)
     */
    @GetMapping("/employees/search-email")
    public ResponseEntity<List<Employee>> searchEmailEmployee(@RequestParam("emailId") String email) {
        List<Employee> employees = employeeRepository.findByEmailIdContainingIgnoreCase(email);
        return ResponseEntity.ok(employees);
    }

    /**
     * Search employees by name or email (LIKE %email%)
     */
    @GetMapping("/employees/keywork")
    public ResponseEntity<List<Employee>> searchKeywordEmployee(@RequestParam("keyword") String keyword) {
        List<Employee> employees = employeeRepository.searchEmployees(keyword);
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
        Page<Employee> employees = employeeRepository.searchByKeywordPageWithMetadata(keyword, pageable);
        // return ResponseEntity.ok(employees);
        // custom response
        return ResponseEntity.ok(PagedResponse.fromPage(employees));

        // List<Employee> employees = employeeRepository.searchByKeywordPage(keyword,
        // pageable);
        // return ResponseEntity.ok(employees);
    }

    /**
     * Search employees by firstName or lastName
     */
    @GetMapping("/employees/email")
    public ResponseEntity<List<Employee>> findByEmail(@RequestParam("email") String name) {
        List<Employee> employees = employeeRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
        return ResponseEntity.ok(employees);
    }

    /**
     * Search employees by firstName or lastName
     */
    @GetMapping("/employees/firstName")
    public ResponseEntity<List<Employee>> findByFirstName(@RequestParam("firstName") String name) {
        List<Employee> employees = employeeRepository
                .findByFirstName(name);
        return ResponseEntity.ok(employees);
    }

}
