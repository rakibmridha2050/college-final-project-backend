package com.rakib.collegeERPsystem.dto.exam;

import java.time.LocalDateTime;
import java.util.List;

public class ExamResponseDTO {
    private Long examId;
    private String examTitle;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private Double totalMarks;
    private Boolean isPublished;
    private Long courseId;
    private String courseName;
    private List<ExamResultResponseDTO> results;
}
