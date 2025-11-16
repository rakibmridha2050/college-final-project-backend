package com.rakib.collegeERPsystem.dto;


import lombok.Data;

import java.time.LocalDate;
@Data
public class AttendanceFilterRequest {
    private Long departmentId;
    private Long classId;
    private Long sectionId;
    private Long courseId;
    private LocalDate attendanceDate;
    private Integer periodNumber;

}

