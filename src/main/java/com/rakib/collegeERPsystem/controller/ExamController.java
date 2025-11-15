package com.rakib.collegeERPsystem.controller;




import com.rakib.collegeERPsystem.dto.exam.ExamCreateDTO;
import com.rakib.collegeERPsystem.dto.exam.ExamDTO;
import com.rakib.collegeERPsystem.dto.exam.ExamUpdateDTO;
import com.rakib.collegeERPsystem.entity.exam.Exam;

import com.rakib.collegeERPsystem.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
public class ExamController {



    private final ExamService examService;

    @PostMapping
    public ResponseEntity<ExamDTO> createExam(@Valid @RequestBody ExamCreateDTO examCreateDTO,
                                              @RequestHeader("X-Faculty-Id") Long facultyId) {
        ExamDTO createdExam = examService.createExam(examCreateDTO, facultyId);
        return ResponseEntity.ok(createdExam);
    }

    @PutMapping("/{examId}")
    public ResponseEntity<ExamDTO> updateExam(@PathVariable Long examId,
                                              @Valid @RequestBody ExamUpdateDTO examUpdateDTO,
                                              @RequestHeader("X-Faculty-Id") Long facultyId) {
        ExamDTO updatedExam = examService.updateExam(examId, examUpdateDTO, facultyId);
        return ResponseEntity.ok(updatedExam);
    }

    @GetMapping("/faculty")
    public ResponseEntity<List<ExamDTO>> getFacultyExams(@RequestHeader("X-Faculty-Id") Long facultyId) {
        List<ExamDTO> exams = examService.getExamsByFaculty(facultyId);
        return ResponseEntity.ok(exams);
    }

    @GetMapping("/{examId}")
    public ResponseEntity<ExamDTO> getExam(@PathVariable Long examId,
                                           @RequestHeader("X-Faculty-Id") Long facultyId) {
        ExamDTO exam = examService.getExamById(examId, facultyId);
        return ResponseEntity.ok(exam);
    }

    @GetMapping("/course/{courseId}")
    public ResponseEntity<List<ExamDTO>> getCourseExams(@PathVariable Long courseId) {
        List<ExamDTO> exams = examService.getExamsByCourse(courseId);
        return ResponseEntity.ok(exams);
    }

    @DeleteMapping("/{examId}")
    public ResponseEntity<Void> deleteExam(@PathVariable Long examId,
                                           @RequestHeader("X-Faculty-Id") Long facultyId) {
        examService.deleteExam(examId, facultyId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{examId}/publish")
    public ResponseEntity<Void> publishExam(@PathVariable Long examId,
                                            @RequestHeader("X-Faculty-Id") Long facultyId) {
        examService.publishExam(examId, facultyId);
        return ResponseEntity.ok().build();
    }
}

