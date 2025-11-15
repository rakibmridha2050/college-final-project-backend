package com.rakib.collegeERPsystem.dto.exam;


import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class ExamCreateDTO {
    @NotBlank(message = "Exam title is required")
    private String examTitle;

    private String description;

    @NotNull(message = "Exam date is required")
    private LocalDateTime examDate;

    @NotNull(message = "Duration is required")
    @Min(value = 1)
    private Integer duration;

    @NotNull(message = "Total marks are required")
    @Positive
    private Double totalMarks;

    @NotBlank(message = "Exam type is required")
    private String examType;

    @NotNull(message = "Course ID is required")
    private Long courseId;
}
