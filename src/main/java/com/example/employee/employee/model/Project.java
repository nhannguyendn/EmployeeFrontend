package com.example.employee.employee.model;

import java.util.List;

import com.example.employee.employee.converter.ListToJsonConverter;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;
import jakarta.persistence.Convert;
import jakarta.persistence.Column;

@Entity
@Table(name = "projects")
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String descriptions;

    @ManyToMany(mappedBy = "projects")
    private List<Employee> employees; // Demo <-choose-> employeeIds

    @Column(name = "employee_ids", columnDefinition = "json")
    @Convert(converter = ListToJsonConverter.class)
    private List<Long> employeeIds; // Demo

    public Project() {

    }

    public Project(String name, String descriptions, List<Employee> employees, List<Long> employeeIds) {
        super();
        this.name = name;
        this.descriptions = descriptions;
        this.employees = employees;
        this.employeeIds = employeeIds;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setDescriptions(String descriptions) {
        this.descriptions = descriptions;
    }

    public String getDescriptions() {
        return descriptions;
    }

    public void setEmployees(List<Employee> employees) {
        this.employees = employees;
    }

    public List<Employee> getEmployees() {
        return employees;
    }

    public void setEmployeeIds(List<Long> employeeIds) {
        this.employeeIds = employeeIds;
    }

    public List<Long> getEmployeeIds() {
        return employeeIds;
    }
}
