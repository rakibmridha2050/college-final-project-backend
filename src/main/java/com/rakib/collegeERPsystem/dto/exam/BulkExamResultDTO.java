package com.rakib.collegeERPsystem.dto.exam;

import jakarta.validation.constraints.*;
import lombok.*;

@Getter
@Setter
public class BulkExamResultDTO {
    @NotNull(message = "Exam ID is required")
    private Long examId;

    @NotNull(message = "Results list is required")
    private java.util.List<ExamResultCreateDTO> results;
}