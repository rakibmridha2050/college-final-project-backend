package com.rakib.collegeERPsystem.controller;


import com.rakib.collegeERPsystem.dto.AttendanceFilterRequest;
import com.rakib.collegeERPsystem.dto.AttendanceRequestDTO;
import com.rakib.collegeERPsystem.dto.BulkAttendanceRequest;
import com.rakib.collegeERPsystem.dto.StudentAttendanceDTO;
import com.rakib.collegeERPsystem.entity.Attendance;
import com.rakib.collegeERPsystem.service.AttendanceService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/attendances")
public class AttendanceController {


    private final AttendanceService attendanceService;

    public AttendanceController(AttendanceService attendanceService) {
        this.attendanceService = attendanceService;
    }

    @PostMapping("/students")
    public ResponseEntity<List<StudentAttendanceDTO>> getStudentsForAttendance(
            @RequestBody AttendanceFilterRequest filterRequest) {

        List<StudentAttendanceDTO> students = attendanceService.getStudentsForAttendance(filterRequest);
        return ResponseEntity.ok(students);
    }

    @PostMapping("/record")
    public ResponseEntity<String> recordAttendance(@RequestBody BulkAttendanceRequest request) {
        attendanceService.recordBulkAttendance(request);
        return ResponseEntity.ok("Attendance recorded successfully");
    }

    @PutMapping("/update")
    public ResponseEntity<String> updateAttendance(@RequestBody BulkAttendanceRequest request) {
        // For update, we extract the necessary info and call update service
        List<StudentAttendanceDTO> records = request.getAttendanceRecords();
        attendanceService.updateAttendance(records, request.getCourseId(), request.getSectionId(),
                request.getAttendanceDate(), request.getPeriodNumber());
        return ResponseEntity.ok("Attendance updated successfully");
    }
    }

