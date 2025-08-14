package com.example.employee.employee.respository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.employee.employee.model.Employee;

@Repository
public interface EmployeeRespository extends JpaRepository<Employee, Long>{

    
} 
