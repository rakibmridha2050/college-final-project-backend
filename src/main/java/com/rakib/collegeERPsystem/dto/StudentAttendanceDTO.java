package com.rakib.collegeERPsystem.dto;

import com.rakib.collegeERPsystem.enums.AttendanceStatus;
import lombok.Data;

@Data
public class StudentAttendanceDTO {
    private Long studentId;
    private String studentRoll;
    private String studentName;
    private String email;
    private AttendanceStatus status;
    private String remarks;

}
