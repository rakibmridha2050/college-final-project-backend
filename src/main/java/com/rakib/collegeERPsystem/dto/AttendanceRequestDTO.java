package com.rakib.collegeERPsystem.dto;

import com.rakib.collegeERPsystem.enums.AttendanceStatus;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class AttendanceRequestDTO {
    private Long studentId;
    private Long courseId;
    private Long sectionId;
    private LocalDate attendanceDate;
    private String status;  // Changed to String to match frontend
    private String remarks;
    private String recordedBy;
    private Integer periodNumber;

}
