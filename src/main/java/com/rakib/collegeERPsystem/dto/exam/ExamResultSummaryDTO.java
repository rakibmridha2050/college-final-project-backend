package com.rakib.collegeERPsystem.dto.exam;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamResultSummaryDTO {
    private Long examId;
    private String examTitle;
    private Long totalStudents;
    private Long studentsAppeared;
    private Long studentsPassed;
    private Long studentsFailed;
    private Long studentsAbsent;
    private Double averageMarks;
    private Double highestMarks;
    private Double lowestMarks;
}
