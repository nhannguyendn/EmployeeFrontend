package com.example.employee.employee.model;

import java.util.List;

import jakarta.persistence.*;

@Entity
@Table(name = "employees")
@NamedQueries({
		@NamedQuery(name = "Employee.findByEmail", query = "SELECT e FROM Employee e WHERE LOWER(e.emailId) = LOWER(:email)"),
		@NamedQuery(name = "Employee.findByFirstName", query = "SELECT e FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))")
})
public class Employee {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "first_name")
	private String firstName;

	@Column(name = "last_name")
	private String lastName;

	@Column(name = "email_id")
	private String emailId;

	@OneToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "card_id") // Foreign key in employees
	private EmployeeCard card;

	@ManyToMany
	@JoinTable(name = "employee_project", // table intermediary
			joinColumns = @JoinColumn(name = "employee_id"), // Foreign key to table Employee
			inverseJoinColumns = @JoinColumn(name = "project_id") // Foreign key in to table Project
	)
	private List<Project> projects;

	@ManyToOne
	@JoinColumn(name = "team_id")
	private Team team;

	public Employee() {

	}

	public Employee(String firstName, String lastName, String emailId) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}
}
