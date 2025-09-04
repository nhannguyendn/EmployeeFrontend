package com.example.employee.salary.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
import org.springframework.web.bind.annotation.RestController;

import com.example.employee.employee.repository.EmployeeRepository;
import com.example.employee.exception.ResoureNotFoundException;
import com.example.employee.salary.model.Salary;
import com.example.employee.salary.repository.SalaryRepository;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("api/v1/")
public class SalaryController {

    private static final Logger logger = LoggerFactory.getLogger(SalaryController.class);

    @Autowired
    private SalaryRepository salaryRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * get salarys
     * 
     * @return listSalarys
     */
    @GetMapping("/salarys")
    public List<Salary> getAllSalary() {
        logger.info("getAllSalary");
        return salaryRepository.findAll();
    }

    /**
     * Create salary
     */
    @PostMapping("/salarys")
    @Transactional("salaryTransactionManager")
    public ResponseEntity<Salary> createSalary(@RequestBody Salary salary) {
        if (!employeeRepository.existsById(salary.getEmployeeId())) {
            return ResponseEntity.ok(null);
        }

        Salary savedSalary = salaryRepository.findByEmployeeId(salary.getEmployeeId())
                .map(existing -> {
                    existing.setEmailId(salary.getEmailId());
                    existing.setSalary(salary.getSalary());
                    return salaryRepository.save(existing);
                })
                .orElseGet(() -> salaryRepository.save(salary));

        return ResponseEntity.ok(savedSalary);
    }

    /**
     * Get Salary by id
     */
    @GetMapping("/salarys/{employeeId}")
    @Transactional(transactionManager = "attendanceTransactionManager", readOnly = true)
    public ResponseEntity<Salary> getSalaryById(@PathVariable Long employeeId) {
        return salaryRepository.findByEmployeeId(employeeId).map(salary -> ResponseEntity.ok(salary))
                .orElse(ResponseEntity.notFound().build());
    }

    /**
     * Update Salary with id
     */
    @PutMapping("/salarys/{employeeId}")
    @Transactional("salaryTransactionManager")
    public ResponseEntity<Salary> updateSalarys(@PathVariable Long employeeId,
            @RequestBody Salary salaryDetails) {
        Salary salary = salaryRepository.findByEmployeeId(employeeId)
                .orElseThrow(() -> new ResoureNotFoundException("Not found Salary id=" + employeeId));

        salary.setSalary(salaryDetails.getSalary());
        Salary salaryUpdated = salaryRepository.save(salary);
        return ResponseEntity.ok(salaryUpdated);
    }

    /**
     * Delete salary with id
     */

    @DeleteMapping("/salarys/{employeeId}")
    @Transactional("salaryTransactionManager")
    public ResponseEntity<Map<String, Boolean>> deleteSalary(@PathVariable Long employeeId) {
        Map<String, Boolean> result = new HashMap<>();

        Optional<Salary> employeeOpt = salaryRepository.findByEmployeeId(employeeId);

        if (employeeOpt.isPresent()) {
            salaryRepository.delete(employeeOpt.get());
            result.put("Deleted", true);
        } else {
            result.put("Deleted", false);
        }
        return ResponseEntity.ok(result);
    }

}
