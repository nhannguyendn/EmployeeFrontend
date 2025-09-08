package com.example.employee.employee.model;

import java.util.List;

// import com.fasterxml.jackson.annotation.JsonIdentityInfo;
// import com.fasterxml.jackson.annotation.ObjectIdGenerators;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

// @JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "id")
@Entity
@Table(name = "employees")
@NamedQueries({
		@NamedQuery(name = "Employee.findByEmail", query = "SELECT e FROM Employee e WHERE LOWER(e.emailId) = LOWER(:email)"),
		@NamedQuery(name = "Employee.findByFirstName", query = "SELECT e FROM Employee e WHERE LOWER(e.firstName) LIKE LOWER(CONCAT('%', :firstName, '%'))")
})
@Data
@Builder            
@NoArgsConstructor
@AllArgsConstructor
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

    @Enumerated(EnumType.STRING)
    private Role role;

	@JsonManagedReference
	@OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
	@JoinColumn(name = "card_id") // Foreign key in employees
	private EmployeeCard card;

	@JsonIgnoreProperties("employees")
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name = "employee_project", // table intermediary
			joinColumns = @JoinColumn(name = "employee_id"), // Foreign key to table Employee
			inverseJoinColumns = @JoinColumn(name = "project_id") // Foreign key in to table Project
	)
	private List<Project> projects;

	@ManyToOne()
	@JoinColumn(name = "team_id")
	@JsonIgnoreProperties("employees")
	private Team team;

	public Employee(String firstName, String lastName, String emailId, Role role) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.emailId = emailId;
		this.role = role;
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

	public void setCard(EmployeeCard card) {
		this.card = card;
	}

	public EmployeeCard getCard() {
		return card;
	}

	public void setTeam(Team team) {
		this.team = team;
	}

	public Team getTeam() {
		return team;
	}

	public void setProjects(List<Project> projects) {
		this.projects = projects;
	}

	public List<Project> getProjects() {
		return projects;
	}

	public void setRole(Role role) {
		this.role = role;
	}

	public Role getRole() {
		return role;
	}

}
