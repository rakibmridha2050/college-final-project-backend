package com.rakib.collegeERPsystem.service;



import com.rakib.collegeERPsystem.dto.exam.BulkExamResultDTO;
import com.rakib.collegeERPsystem.dto.exam.ExamResultCreateDTO;
import com.rakib.collegeERPsystem.dto.exam.ExamResultDTO;
import com.rakib.collegeERPsystem.dto.exam.ExamResultSummaryDTO;
import com.rakib.collegeERPsystem.entity.Student;
import com.rakib.collegeERPsystem.entity.exam.Exam;
import com.rakib.collegeERPsystem.entity.exam.ExamResult;
import com.rakib.collegeERPsystem.entity.exam.ResultStatus;
import com.rakib.collegeERPsystem.repository.StudentRepository;
import com.rakib.collegeERPsystem.repository.exam.ExamRepository;
import com.rakib.collegeERPsystem.repository.exam.ExamResultRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Transactional
public class ExamResultService {

    private final ExamResultRepository examResultRepository;
    private final ExamRepository examRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public void createBulkExamResults(BulkExamResultDTO bulkDTO, Long facultyId) {
        Exam exam = examRepository.findByIdAndFacultyId(bulkDTO.getExamId(), facultyId)
                .orElseThrow(() -> new RuntimeException("Exam not found or you don't have permission"));

        if (!exam.getIsPublished()) {
            throw new RuntimeException("Cannot add results to unpublished exam");
        }

        for (ExamResultCreateDTO resultDTO : bulkDTO.getResults()) {
            Student student = studentRepository.findById(resultDTO.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found with ID: " + resultDTO.getStudentId()));

            // Check if result already exists
            if (examResultRepository.existsByExamIdAndStudentId(exam.getId(), student.getId())) {
                throw new RuntimeException("Result already exists for student: " + student.getName());
            }

            ExamResult examResult = new ExamResult();
            examResult.setExam(exam);
            examResult.setStudent(student);
            examResult.setMarksObtained(resultDTO.getMarksObtained());
            examResult.setRemarks(resultDTO.getRemarks());

            // Calculate grade and status
            calculateGradeAndStatus(examResult, exam.getTotalMarks());

            examResultRepository.save(examResult);
        }
    }

    public ExamResultDTO updateExamResult(Long resultId, ExamResultCreateDTO resultDTO, Long facultyId) {
        ExamResult examResult = examResultRepository.findById(resultId)
                .orElseThrow(() -> new RuntimeException("Exam result not found"));

        // Verify faculty owns the exam
        if (!examResult.getExam().getFaculty().getId().equals(facultyId)) {
            throw new RuntimeException("You don't have permission to update this result");
        }

        examResult.setMarksObtained(resultDTO.getMarksObtained());
        examResult.setRemarks(resultDTO.getRemarks());

        // Recalculate grade and status
        calculateGradeAndStatus(examResult, examResult.getExam().getTotalMarks());

        ExamResult updatedResult = examResultRepository.save(examResult);
        return convertToDTO(updatedResult);
    }

    public List<ExamResultDTO> getResultsByExam(Long examId, Long facultyId) {
        Exam exam = examRepository.findByIdAndFacultyId(examId, facultyId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        return examResultRepository.findByExamId(examId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ExamResultDTO> getResultsByStudent(Long studentId) {
        return examResultRepository.findByStudentId(studentId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ExamResultSummaryDTO getExamSummary(Long examId, Long facultyId) {
        Exam exam = examRepository.findByIdAndFacultyId(examId, facultyId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        ExamResultSummaryDTO summary = new ExamResultSummaryDTO();
        summary.setExamId(examId);
        summary.setExamTitle(exam.getExamTitle());

        // Get all students enrolled in the course
        Long totalStudents = (long) exam.getCourse().getStudents().size();
        summary.setTotalStudents(totalStudents);

        // Calculate statistics
        summary.setStudentsAppeared(examResultRepository.countByExamIdAndStatus(examId, ResultStatus.PASSED) +
                examResultRepository.countByExamIdAndStatus(examId, ResultStatus.FAILED));

        summary.setStudentsPassed(examResultRepository.countByExamIdAndStatus(examId, ResultStatus.PASSED));
        summary.setStudentsFailed(examResultRepository.countByExamIdAndStatus(examId, ResultStatus.FAILED));
        summary.setStudentsAbsent(examResultRepository.countByExamIdAndStatus(examId, ResultStatus.ABSENT));

        summary.setAverageMarks(examResultRepository.findAverageMarksByExamId(examId));
        summary.setHighestMarks(examResultRepository.findHighestMarksByExamId(examId));
        summary.setLowestMarks(examResultRepository.findLowestMarksByExamId(examId));

        return summary;
    }

    private void calculateGradeAndStatus(ExamResult examResult, Double totalMarks) {
        Double percentage = (examResult.getMarksObtained() / totalMarks) * 100;

        // Set grade based on percentage
        if (percentage >= 80) {
            examResult.setGrade("A+");
            examResult.setGpa(4.0);
        } else if (percentage >= 75) {
            examResult.setGrade("A");
            examResult.setGpa(3.75);
        } else if (percentage >= 70) {
            examResult.setGrade("A-");
            examResult.setGpa(3.5);
        } else if (percentage >= 65) {
            examResult.setGrade("B+");
            examResult.setGpa(3.25);
        } else if (percentage >= 60) {
            examResult.setGrade("B");
            examResult.setGpa(3.0);
        } else if (percentage >= 55) {
            examResult.setGrade("B-");
            examResult.setGpa(2.75);
        } else if (percentage >= 50) {
            examResult.setGrade("C+");
            examResult.setGpa(2.5);
        } else if (percentage >= 45) {
            examResult.setGrade("C");
            examResult.setGpa(2.25);
        } else if (percentage >= 40) {
            examResult.setGrade("D");
            examResult.setGpa(2.0);
        } else {
            examResult.setGrade("F");
            examResult.setGpa(0.0);
        }

        // Set status
        if (percentage >= 40) {
            examResult.setStatus(ResultStatus.PASSED);
        } else {
            examResult.setStatus(ResultStatus.FAILED);
        }
    }

    private ExamResultDTO convertToDTO(ExamResult examResult) {
        ExamResultDTO dto = new ExamResultDTO();
        dto.setId(examResult.getId());
        dto.setExamId(examResult.getExam().getId());
        dto.setStudentId(examResult.getStudent().getId());
        dto.setMarksObtained(examResult.getMarksObtained());
        dto.setGrade(examResult.getGrade());
        dto.setGpa(examResult.getGpa());
        dto.setStatus(examResult.getStatus().name());
        dto.setRemarks(examResult.getRemarks());
        dto.setStudentName(examResult.getStudent().getName());
        dto.setStudentIdNumber(examResult.getStudent().getStudentId());
        dto.setExamTitle(examResult.getExam().getExamTitle());
        dto.setTotalMarks(examResult.getExam().getTotalMarks());
        return dto;
    }


}
