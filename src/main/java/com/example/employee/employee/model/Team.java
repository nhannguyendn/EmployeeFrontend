package com.example.employee.employee.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String descriptions;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private List<Employee> employees;

    public Team() {

    }

    public Team(String name, String descriptions, List<Employee> employees) {
        super();
        this.name = name;
        this.descriptions = descriptions;
        this.employees = employees;
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
}
