package com.example.employee.attendance.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.employee.attendance.model.Attendance;


@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {

    Optional<Attendance> findById(long id);

    List<Attendance> findByEmployeeId(long employeeId);

    List<Attendance> findByEmployeeIdIn(List<Long> employeeIds);
}
