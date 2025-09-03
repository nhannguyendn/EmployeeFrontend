package com.example.employee.salary.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "salarys")
public class Salary {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "employee_id")
	private long employeeId;

	@Column(name = "email_id")
	private String emailId;

	@Column (name = "salary")
	private long salary;

	public Salary() {

	}

	public Salary(long employeeId,  String emailId, long salary) {
		super();
		this.employeeId = employeeId;
		this.emailId = emailId;
		this.salary = salary;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public void setSalary(long salary) {
		this.salary = salary;
	}

	public long getSalary() {
		return salary;
	}

}
