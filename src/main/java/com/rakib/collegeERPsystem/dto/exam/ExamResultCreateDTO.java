package com.rakib.collegeERPsystem.dto.exam;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExamResultCreateDTO {
    @NotNull(message = "Student ID is required")
    private Long studentId;

    @NotNull(message = "Marks obtained are required")
    private Double marksObtained;

    private String remarks;
}