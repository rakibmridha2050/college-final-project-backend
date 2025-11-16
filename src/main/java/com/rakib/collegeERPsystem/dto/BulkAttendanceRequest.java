package com.rakib.collegeERPsystem.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class BulkAttendanceRequest {
    private Long courseId;
    private Long sectionId;
    private LocalDate attendanceDate;
    private Integer periodNumber;
    private String recordedBy;
    private List<StudentAttendanceDTO> attendanceRecords;


}
