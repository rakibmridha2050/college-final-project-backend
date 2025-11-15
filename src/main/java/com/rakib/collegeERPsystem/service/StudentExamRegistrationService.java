package com.rakib.collegeERPsystem.service;

import com.rakib.collegeERPsystem.dto.exam.StudentAnswerDTO;
import com.rakib.collegeERPsystem.dto.exam.StudentExamRegistrationDTO;
import com.rakib.collegeERPsystem.entity.Student;
import com.rakib.collegeERPsystem.entity.exam.Exam;
import com.rakib.collegeERPsystem.entity.exam.StudentAnswer;
import com.rakib.collegeERPsystem.entity.exam.StudentExamRegistration;
import com.rakib.collegeERPsystem.repository.StudentRepository;
import com.rakib.collegeERPsystem.repository.exam.ExamRepository;
import com.rakib.collegeERPsystem.repository.exam.StudentExamRegistrationRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class StudentExamRegistrationService {

    private final StudentExamRegistrationRepository registrationRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;

    /**
     * Register a student for an exam
     */
//    public StudentExamRegistrationDTO registerStudent(Long examId, Long studentId) {
//        Exam exam = examRepository.findById(examId)
//                .orElseThrow(() -> new EntityNotFoundException("Exam not found with ID: " + examId));
//
//        Student student = studentRepository.findById(studentId)
//                .orElseThrow(() -> new EntityNotFoundException("Student not found with ID: " + studentId));
//
//        // Check if already registered
//        registrationRepository.findByExam_ExamIdAndStudent_Id(examId, studentId)
//                .ifPresent(r -> {
//                    throw new IllegalStateException("Student is already registered for this exam.");
//                });
//
//        StudentExamRegistration registration = StudentExamRegistration.builder()
//                .exam(exam)
//                .student(student)
//                .registrationDate(LocalDateTime.now())
//                .build();
//
//        StudentExamRegistration saved = registrationRepository.save(registration);
//        return convertToDTO(saved);
//    }

    /**
     * Get all registrations for a specific exam
     */
//    @Transactional(readOnly = true)
//    public List<StudentExamRegistrationDTO> getRegistrationsByExam(Long examId) {
//        List<StudentExamRegistration> registrations = registrationRepository.findByExam_ExamId(examId);
//        return registrations.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }

    /**
     * Get all registrations for a specific student
     */
//    @Transactional(readOnly = true)
//    public List<StudentExamRegistrationDTO> getRegistrationsByStudent(Long studentId) {
//        List<StudentExamRegistration> registrations = registrationRepository.findByStudent_Id(studentId);
//        return registrations.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }

    /**
     * Get a single registration by exam + student
     */
//    @Transactional(readOnly = true)
//    public StudentExamRegistrationDTO getRegistration(Long examId, Long studentId) {
//        StudentExamRegistration registration = registrationRepository
//                .findByExam_ExamIdAndStudent_Id(examId, studentId)
//                .orElseThrow(() -> new EntityNotFoundException(
//                        "Registration not found for examId=" + examId + " and studentId=" + studentId
//                ));
//        return convertToDTO(registration);
//    }

    /**
     * Delete a registration by ID
     */
    public void deleteRegistration(Long registrationId) {
        if (!registrationRepository.existsById(registrationId)) {
            throw new EntityNotFoundException("Registration not found with ID: " + registrationId);
        }
        registrationRepository.deleteById(registrationId);
    }

    /**
     * Convert Entity → DTO
     */
    private StudentExamRegistrationDTO convertToDTO(StudentExamRegistration registration) {
        return StudentExamRegistrationDTO.builder()
                .registrationId(registration.getRegistrationId())
                .registrationDate(registration.getRegistrationDate())
                .studentId(registration.getStudent() != null ? registration.getStudent().getId() : null)
                .studentName(registration.getStudent() != null ? registration.getStudent().getName() : null)
//                .examId(registration.getExam() != null ? registration.getExam().getExamId() : null)
                .examTitle(registration.getExam() != null ? registration.getExam().getExamTitle() : null) // ✅ FIXED
                .answers(registration.getAnswers() != null
                        ? registration.getAnswers().stream()
                        .map(this::convertAnswerToDTO)
                        .collect(Collectors.toList())
                        : null)
                .build();
    }

    private StudentAnswerDTO convertAnswerToDTO(StudentAnswer answer) {
        return StudentAnswerDTO.builder()
                .answerId(answer.getAnswerId())
                .questionId(answer.getQuestion() != null ? answer.getQuestion().getQuestionId() : null)
                .questionText(answer.getQuestion() != null ? answer.getQuestion().getQuestionText() : null)
                .chosenOption(answer.getChosenOption())  // ✅ matches entity & DTO
                .isCorrect(answer.getIsCorrect())
                .build();
    }


}