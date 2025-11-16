package com.rakib.collegeERPsystem.dto.exam;

import lombok.Data;

@Data
public class StudentExamResultDTO {
    private String studentId;
    private String name;
    private String program;
    private String deptName;
    private String deptCode;
    private String examTitle;
    private Double totalMarks;
    private String gender;
    private String email;
    private Integer currentSemester;
    private Double marksObtained;
    private String grade;
    private Double gpa;
    private String remarks;
    private String status;
    private String courseName;

    public StudentExamResultDTO(String studentId, String name, String program, String deptName, String deptCode,
                                String examTitle, Double totalMarks, String gender, String email, Integer currentSemester,
                                Double marksObtained, String grade, Double gpa, String remarks, String status,
                                String courseName) {
        this.studentId = studentId;
        this.name = name;
        this.program = program;
        this.deptName = deptName;
        this.deptCode = deptCode;
        this.examTitle = examTitle;
        this.totalMarks = totalMarks;
        this.gender = gender;
        this.email = email;
        this.currentSemester = currentSemester;
        this.marksObtained = marksObtained;
        this.grade = grade;
        this.gpa = gpa;
        this.remarks = remarks;
        this.status = status;
        this.courseName = courseName;
    }

    // Getters and setters
    // (Omitted for brevity; generate using your IDE)
}
