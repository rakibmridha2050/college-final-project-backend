package com.rakib.collegeERPsystem.service;


import com.rakib.collegeERPsystem.dto.exam.ExamCreateDTO;
import com.rakib.collegeERPsystem.dto.exam.ExamDTO;
import com.rakib.collegeERPsystem.dto.exam.ExamUpdateDTO;
import com.rakib.collegeERPsystem.entity.Course;
import com.rakib.collegeERPsystem.entity.Faculty;
import com.rakib.collegeERPsystem.entity.exam.Exam;

import com.rakib.collegeERPsystem.entity.exam.ExamResult;
import com.rakib.collegeERPsystem.entity.exam.ExamType;
import com.rakib.collegeERPsystem.repository.CourseRepository;
import com.rakib.collegeERPsystem.repository.FacultyRepository;
import com.rakib.collegeERPsystem.repository.StudentRepository;
import com.rakib.collegeERPsystem.repository.exam.ExamRepository;

import com.rakib.collegeERPsystem.repository.exam.ExamResultRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;


@Service
@RequiredArgsConstructor
@Slf4j
public class ExamService {
    private final ExamRepository examRepository;
    private final CourseRepository courseRepository;
    private final FacultyRepository facultyRepository;
    private final StudentRepository studentRepository;
    private final ExamResultRepository examResultRepository;

    public ExamDTO createExam(ExamCreateDTO examCreateDTO, Long facultyId) {
        Course course = courseRepository.findById(examCreateDTO.getCourseId())
                .orElseThrow(() -> new RuntimeException("Course not found"));

        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found"));

        // Check if faculty teaches this course
        if (!faculty.getCourses().contains(course)) {
            throw new RuntimeException("Faculty does not teach this course");
        }

        // Check for duplicate exam
        if (examRepository.existsByCourseIdAndExamTitleAndExamDateBetween(
                examCreateDTO.getCourseId(),
                examCreateDTO.getExamTitle(),
                examCreateDTO.getExamDate().minusDays(1),
                examCreateDTO.getExamDate().plusDays(1))) {
            throw new RuntimeException("An exam with same title already exists for this course around the same date");
        }

        Exam exam = new Exam();
        exam.setExamTitle(examCreateDTO.getExamTitle());
        exam.setDescription(examCreateDTO.getDescription());
        exam.setExamDate(examCreateDTO.getExamDate());
        exam.setDuration(examCreateDTO.getDuration());
        exam.setTotalMarks(examCreateDTO.getTotalMarks());
        exam.setExamType(ExamType.valueOf(examCreateDTO.getExamType()));
        exam.setCourse(course);
        exam.setFaculty(faculty);
        exam.setIsPublished(false);

        Exam savedExam = examRepository.save(exam);
        return convertToDTO(savedExam);
    }

    public ExamDTO updateExam(Long examId, ExamUpdateDTO examUpdateDTO, Long facultyId) {
        Exam exam = examRepository.findByIdAndFacultyId(examId, facultyId)
                .orElseThrow(() -> new RuntimeException("Exam not found or you don't have permission to update it"));

        exam.setExamTitle(examUpdateDTO.getExamTitle());
        exam.setDescription(examUpdateDTO.getDescription());
        exam.setExamDate(examUpdateDTO.getExamDate());
        exam.setDuration(examUpdateDTO.getDuration());
        exam.setTotalMarks(examUpdateDTO.getTotalMarks());

        if (examUpdateDTO.getIsPublished() != null) {
            exam.setIsPublished(examUpdateDTO.getIsPublished());
        }

        Exam updatedExam = examRepository.save(exam);
        return convertToDTO(updatedExam);
    }

    public List<ExamDTO> getExamsByFaculty(Long facultyId) {
        return examRepository.findByFacultyIdOrderByExamDateDesc(facultyId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<ExamDTO> getExamsByCourse(Long courseId) {
        return examRepository.findByCourseIdOrderByExamDateDesc(courseId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public ExamDTO getExamById(Long examId, Long facultyId) {
        Exam exam = examRepository.findByIdAndFacultyId(examId, facultyId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));
        return convertToDTO(exam);
    }

    @Transactional
    public void deleteExam(Long examId, Long facultyId) {
        Exam exam = examRepository.findByIdAndFacultyId(examId, facultyId)
                .orElseThrow(() -> new RuntimeException("Exam not found or you don't have permission to delete it"));

        // Delete associated results first
        examResultRepository.deleteAll(exam.getExamResults());
        examRepository.delete(exam);
    }

    @Transactional
    public void publishExam(Long examId, Long facultyId) {
        Exam exam = examRepository.findByIdAndFacultyId(examId, facultyId)
                .orElseThrow(() -> new RuntimeException("Exam not found"));

        exam.setIsPublished(true);
        examRepository.save(exam);
    }

    private ExamDTO convertToDTO(Exam exam) {
        ExamDTO dto = new ExamDTO();
        dto.setId(exam.getId());
        dto.setExamTitle(exam.getExamTitle());
        dto.setDescription(exam.getDescription());
        dto.setExamDate(exam.getExamDate());
        dto.setDuration(exam.getDuration());
        dto.setTotalMarks(exam.getTotalMarks());
        dto.setExamType(exam.getExamType().name());
        dto.setCourseId(exam.getCourse().getId());
        dto.setCourseName(exam.getCourse().getCourseName());
        dto.setFacultyName(exam.getFaculty().getName());
        dto.setIsPublished(exam.getIsPublished());
        return dto;
    }
}