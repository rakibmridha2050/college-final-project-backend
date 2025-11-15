package com.rakib.collegeERPsystem.controller;

import com.rakib.collegeERPsystem.dto.exam.BulkExamResultDTO;
import com.rakib.collegeERPsystem.dto.exam.ExamResultCreateDTO;
import com.rakib.collegeERPsystem.dto.exam.ExamResultDTO;
import com.rakib.collegeERPsystem.dto.exam.ExamResultSummaryDTO;
import com.rakib.collegeERPsystem.entity.exam.ExamResult;
import com.rakib.collegeERPsystem.service.ExamResultService;
import com.rakib.collegeERPsystem.service.ExamService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/exam-results")
@RequiredArgsConstructor
public class ExamResultController {

    private final ExamResultService examResultService;

    @PostMapping("/bulk")
    public ResponseEntity<Void> createBulkResults(@Valid @RequestBody BulkExamResultDTO bulkDTO,
                                                  @RequestHeader("X-Faculty-Id") Long facultyId) {
        examResultService.createBulkExamResults(bulkDTO, facultyId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{resultId}")
    public ResponseEntity<ExamResultDTO> updateResult(@PathVariable Long resultId,
                                                      @Valid @RequestBody ExamResultCreateDTO resultDTO,
                                                      @RequestHeader("X-Faculty-Id") Long facultyId) {
        ExamResultDTO updatedResult = examResultService.updateExamResult(resultId, resultDTO, facultyId);
        return ResponseEntity.ok(updatedResult);
    }

    @GetMapping("/exam/{examId}")
    public ResponseEntity<List<ExamResultDTO>> getResultsByExam(@PathVariable Long examId,
                                                                @RequestHeader("X-Faculty-Id") Long facultyId) {
        List<ExamResultDTO> results = examResultService.getResultsByExam(examId, facultyId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<ExamResultDTO>> getResultsByStudent(@PathVariable Long studentId) {
        List<ExamResultDTO> results = examResultService.getResultsByStudent(studentId);
        return ResponseEntity.ok(results);
    }

    @GetMapping("/{examId}/summary")
    public ResponseEntity<ExamResultSummaryDTO> getExamSummary(@PathVariable Long examId,
                                                               @RequestHeader("X-Faculty-Id") Long facultyId) {
        ExamResultSummaryDTO summary = examResultService.getExamSummary(examId, facultyId);
        return ResponseEntity.ok(summary);
    }
}
