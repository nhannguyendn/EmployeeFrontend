package com.example.employee.employee.dto;

import java.util.List;

import com.example.employee.attendance.model.Attendance;
import com.example.employee.employee.model.Employee;
import com.example.employee.employee.model.Team;
import com.example.employee.salary.model.Salary;

public class EmployeeDTO {
    private Long id;
    private String fistName;
    private String lastName;
    private String emailId;
    private TeamDTO teamDTO;
    private Salary salary;
    private List<Attendance> attendances;

    public EmployeeDTO() {

    }

    public EmployeeDTO(Long id, String firstName, String lastName, String emailId) {
        this.id = id;
        this.fistName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
    }

    public EmployeeDTO(Long id, String firstName, String lastName, String emailId, TeamDTO teamDTO) {
        this.id = id;
        this.fistName = firstName;
        this.lastName = lastName;
        this.emailId = emailId;
        this.teamDTO = teamDTO;
    }

    public String getEmailId() {
        return emailId;
    }

    public String getFistName() {
        return fistName;
    }

    public Long getId() {
        return id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public void setFistName(String fistName) {
        this.fistName = fistName;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public void setTeamDTO(TeamDTO teamDTO) {
        this.teamDTO = teamDTO;
    }

    public TeamDTO getTeamDTO() {
        return teamDTO;
    }

    public void setAttendances(List<Attendance> attendances) {
        this.attendances = attendances;
    }

    public List<Attendance> getAttendances() {
        return attendances;
    }

    public void setSalary(Salary salary) {
        this.salary = salary;
    }

    public Salary getSalary() {
        return salary;
    }

    public static EmployeeDTO fromEntity(Employee e) {
        Team team = e.getTeam();
        TeamDTO teamDTO = team != null ? new TeamDTO(team.getId(), team.getName(), team.getDescriptions()) : null;

        EmployeeDTO employeeDTO = new EmployeeDTO(
                e.getId(),
                e.getFirstName(),
                e.getLastName(),
                e.getEmailId(),
                teamDTO);
                return employeeDTO;
    }
}
