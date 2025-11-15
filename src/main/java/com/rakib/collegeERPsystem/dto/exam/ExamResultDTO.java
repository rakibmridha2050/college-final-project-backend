package com.rakib.collegeERPsystem.dto.exam;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class ExamResultDTO {
    private Long id;

    @NotNull(message = "Exam ID is required")
    private Long examId;

    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Marks obtained are required")
    private Double marksObtained;

    private String grade;
    private Double gpa;
    private String status;
    private String remarks;

    // Additional fields for display
    private String studentName;
    private String studentIdNumber;
    private String examTitle;
    private Double totalMarks;
}

