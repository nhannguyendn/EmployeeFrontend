package com.example.employee.attendance.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "attendances")
public class Attendance {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(name = "employee_id")
	private long employeeId;

	@Column(name = "email_id")
	private String emailId;

	@Column(name = "time_attendance")
	private long timeAttendance;

	public Attendance() {

	}

	public Attendance(long employeeId, String emailId, long timeAttendance) {
		super();
		this.employeeId = employeeId;
		this.emailId = emailId;
		this.timeAttendance = timeAttendance;
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

	public void setTimeAttendance(long timeAttendance) {
		this.timeAttendance = timeAttendance;
	}

	public long getTimeAttendance() {
		return timeAttendance;
	}

}
