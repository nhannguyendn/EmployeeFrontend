package com.example.employee.attendance.controller;

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

import com.example.employee.attendance.model.Attendance;
import com.example.employee.attendance.repository.AttendanceRepository;
import com.example.employee.employee.repository.EmployeeRepository;
import com.example.employee.exception.ResoureNotFoundException;

@CrossOrigin(origins = "http://localhost:3000")
@RestController
@RequestMapping("/api/v1")
public class AttendanceController {

    private static final Logger logger = LoggerFactory.getLogger(AttendanceController.class);

    @Autowired
    private AttendanceRepository attendanceRepository;

    @Autowired
    private EmployeeRepository employeeRepository;

    /**
     * get attendance
     * 
     * @return listAttendances
     */
    @GetMapping("/attendances")
    public List<Attendance> getAllAttendance() {
        logger.info("getAllAttendances");
        return attendanceRepository.findAll();
    }

    /**
     * Create Attendance
     */
    @PostMapping("/attendance")
    @Transactional("attendanceTransactionManager")
    public ResponseEntity<Attendance> createAttendance(@RequestBody Attendance attendance) {
        if (!employeeRepository.existsById(attendance.getEmployeeId())) {
            return ResponseEntity.ok(null);
        }
        Attendance attendanceAdded = attendanceRepository.save(attendance);
        return ResponseEntity.ok(attendanceAdded);
    }

    /**
     * Get Attendance by id
     */
    @GetMapping("/attendance/{employeeId}")
    @Transactional(transactionManager = "attendanceTransactionManager", readOnly = true)
    public ResponseEntity<List<Attendance>> getAttendancesById(@PathVariable Long employeeId) {
        List<Attendance> attendances = attendanceRepository.findByEmployeeId(employeeId);
        if (attendances.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(attendances);
    }

    /**
     * Update Attendance with id
     */
    @PutMapping("/attendance/{attendanceId}")
    @Transactional("attendanceTransactionManager")
    public ResponseEntity<Attendance> updateAttendances(@PathVariable Long attendanceId,
            @RequestBody Attendance attendanceDetails) {
        Attendance attendance = attendanceRepository.findById(attendanceId)
                .orElseThrow(() -> new ResoureNotFoundException("Not found Attendance id=" + attendanceId));

        attendance.setTimeAttendance(attendanceDetails.getTimeAttendance());
        Attendance employeeUpdated = attendanceRepository.save(attendance);
        return ResponseEntity.ok(employeeUpdated);
    }

    /**
     * Delete attenddance with id
     */

    @DeleteMapping("/attendance/{employeeId}")
    @Transactional("attendanceTransactionManager")
    public ResponseEntity<Map<String, Boolean>> deleteAttendance(@PathVariable Long attendanceId) {
        Map<String, Boolean> result = new HashMap<>();

        Optional<Attendance> employeeOpt = attendanceRepository.findById(attendanceId);

        if (employeeOpt.isPresent()) {
            attendanceRepository.delete(employeeOpt.get());
            result.put("Deleted", true);
        } else {
            result.put("Deleted", false);
        }
        return ResponseEntity.ok(result);
    }

}
