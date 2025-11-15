package com.rakib.collegeERPsystem.controller;

import com.rakib.collegeERPsystem.dto.exam.QuestionDTO;
import com.rakib.collegeERPsystem.service.QuestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams/{examId}/questions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class QuestionController {

    private final QuestionService questionService;

    /**
     * Create a new question for an exam
     */
    @PostMapping
    public ResponseEntity<QuestionDTO> createQuestion(
            @PathVariable Long examId,
            @RequestBody QuestionDTO questionDTO) {

        QuestionDTO createdQuestion = questionService.createQuestion(examId, questionDTO);
        return new ResponseEntity<>(createdQuestion, HttpStatus.CREATED);
    }

    /**
     * Get all questions for a specific exam
     */
//    @GetMapping
//    public ResponseEntity<List<QuestionDTO>> getQuestionsByExam(@PathVariable Long examId) {
//        List<QuestionDTO> questions = questionService.getQuestionsByExam(examId);
//        return ResponseEntity.ok(questions);
//    }

    /**
     * Update a specific question
     */
    @PutMapping("/{questionId}")
    public ResponseEntity<QuestionDTO> updateQuestion(
            @PathVariable Long examId,
            @PathVariable Long questionId,
            @RequestBody QuestionDTO questionDTO) {

        QuestionDTO updated = questionService.updateQuestion(questionId, questionDTO);
        return ResponseEntity.ok(updated);
    }

    /**
     * Delete a question by ID
     */
    @DeleteMapping("/{questionId}")
    public ResponseEntity<Void> deleteQuestion(
            @PathVariable Long examId,
            @PathVariable Long questionId) {

        questionService.deleteQuestion(questionId);
        return ResponseEntity.noContent().build();
    }
}