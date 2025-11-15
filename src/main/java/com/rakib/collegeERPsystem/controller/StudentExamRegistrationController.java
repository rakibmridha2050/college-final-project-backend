package com.rakib.collegeERPsystem.controller;

import com.rakib.collegeERPsystem.dto.exam.StudentExamRegistrationDTO;
import com.rakib.collegeERPsystem.service.StudentExamRegistrationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exams")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", allowedHeaders = "*")
public class StudentExamRegistrationController {

    private final StudentExamRegistrationService registrationService;

    /**
     * Register a student for an exam
     */
//    @PostMapping("/{examId}/registrations/{studentId}")
//    public ResponseEntity<StudentExamRegistrationDTO> registerStudent(
//            @PathVariable Long examId,
//            @PathVariable Long studentId) {
//
//        StudentExamRegistrationDTO registrationDTO = registrationService.registerStudent(examId, studentId);
//        return new ResponseEntity<>(registrationDTO, HttpStatus.CREATED);
//    }

    /**
     * Get all registrations for a specific exam
     */
//    @GetMapping("/{examId}/registrations")
//    public ResponseEntity<List<StudentExamRegistrationDTO>> getRegistrationsByExam(
//            @PathVariable Long examId) {
//
//        List<StudentExamRegistrationDTO> registrations = registrationService.getRegistrationsByExam(examId);
//        return ResponseEntity.ok(registrations);
//    }

    /**
     * Get all registrations for a specific student
     */
//    @GetMapping("/registrations/student/{studentId}")
//    public ResponseEntity<List<StudentExamRegistrationDTO>> getRegistrationsByStudent(
//            @PathVariable Long studentId) {
//
//        List<StudentExamRegistrationDTO> registrations = registrationService.getRegistrationsByStudent(studentId);
//        return ResponseEntity.ok(registrations);
//    }

    /**
     * Get a single registration by exam + student
     */
//    @GetMapping("/{examId}/registrations/{studentId}")
//    public ResponseEntity<StudentExamRegistrationDTO> getRegistration(
//            @PathVariable Long examId,
//            @PathVariable Long studentId) {
//
//        StudentExamRegistrationDTO registrationDTO = registrationService.getRegistration(examId, studentId);
//        return ResponseEntity.ok(registrationDTO);
//    }

    /**
     * Delete a registration by ID
     */
    @DeleteMapping("/registrations/{registrationId}")
    public ResponseEntity<Void> deleteRegistration(@PathVariable Long registrationId) {
        registrationService.deleteRegistration(registrationId);
        return ResponseEntity.noContent().build();
    }
}
