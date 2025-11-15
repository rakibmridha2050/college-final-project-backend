package com.rakib.collegeERPsystem.service;

import com.rakib.collegeERPsystem.dto.StudentDTO;
import com.rakib.collegeERPsystem.dto.StudentResponseDTO;
import com.rakib.collegeERPsystem.entity.*;
import com.rakib.collegeERPsystem.repository.*;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentService {

    private final StudentRepository studentRepository;
    private final DepartmentRepository departmentRepository;
    private final ClassesRepository classesRepository;
    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;

    @Transactional
    public StudentResponseDTO createStudent(StudentDTO studentDTO) {
        log.info("Creating new student: {}", studentDTO.getEmail());

        // Check if student ID or email already exists
        if (studentRepository.existsByStudentId(studentDTO.getStudentId())) {
            throw new RuntimeException("Student ID already exists: " + studentDTO.getStudentId());
        }

        if (studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + studentDTO.getEmail());
        }

        Section section = sectionRepository.findById(studentDTO.getSectionId()).orElseThrow();


        Student student = convertToEntity(studentDTO);
        student.setSection(section);
        Student savedStudent = studentRepository.save(student);

        log.info("Student created successfully with ID: {}", savedStudent.getId());
        return convertToResponseDTO(savedStudent);
    }

    public List<StudentResponseDTO> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public StudentResponseDTO getStudentById(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));
        return convertToResponseDTO(student);
    }
    public List<StudentResponseDTO> getStudentBySectionId(Long id) {
        List<Student> students = studentRepository.findBySectionId(id);

        return students.stream().map(this::convertToResponseDTO).toList();
    }

    public StudentResponseDTO getStudentByStudentId(String studentId) {
        Student student = studentRepository.findByStudentId(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with student ID: " + studentId));
        return convertToResponseDTO(student);
    }

    @Transactional
    public StudentResponseDTO updateStudent(Long id, StudentDTO studentDTO) {
        log.info("Updating student with ID: {}", id);

        Student existingStudent = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));

        // Check if email is being changed and if it already exists
        if (!existingStudent.getEmail().equals(studentDTO.getEmail()) &&
                studentRepository.existsByEmail(studentDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + studentDTO.getEmail());
        }

        updateEntityFromDTO(existingStudent, studentDTO);
        Student updatedStudent = studentRepository.save(existingStudent);

        log.info("Student updated successfully with ID: {}", id);
        return convertToResponseDTO(updatedStudent);
    }

    @Transactional
    public void deleteStudent(Long id) {
        if (!studentRepository.existsById(id)) {
            throw new EntityNotFoundException("Student not found with id: " + id);
        }
        studentRepository.deleteById(id);
        log.info("Student deleted successfully with ID: {}", id);
    }

    @Transactional
    public void softDeleteStudent(Long id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + id));
        student.setIsActive(false);
        studentRepository.save(student);
        log.info("Student soft deleted with ID: {}", id);
    }

    public List<StudentResponseDTO> getStudentsByDepartment(Long departmentId) {
        return studentRepository.findByDepartmentId(departmentId).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<StudentResponseDTO> getStudentsByProgram(String program) {
        return studentRepository.findByProgram(program).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<StudentResponseDTO> getStudentsBySemester(int semester) {
        return studentRepository.findByCurrentSemester(semester).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<StudentResponseDTO> searchStudentsByName(String name) {
        return studentRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    public List<StudentResponseDTO> getActiveStudents() {
        return studentRepository.findByIsActiveTrue().stream()
                .map(this::convertToResponseDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentResponseDTO addCoursesToStudent(Long studentId, List<Long> courseIds) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + studentId));

        List<Course> courses = courseRepository.findAllById(courseIds);
        student.getCourses().addAll(courses);

        Student updatedStudent = studentRepository.save(student);
        return convertToResponseDTO(updatedStudent);
    }

    @Transactional
    public StudentResponseDTO removeCoursesFromStudent(Long studentId, List<Long> courseIds) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new EntityNotFoundException("Student not found with id: " + studentId));

        student.getCourses().removeIf(course -> courseIds.contains(course.getId()));

        Student updatedStudent = studentRepository.save(student);
        return convertToResponseDTO(updatedStudent);
    }

    // Conversion methods
    private Student convertToEntity(StudentDTO dto) {
        Student student = Student.builder()
                .studentId(dto.getStudentId())
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .dob(dto.getDob())
                .gender(dto.getGender())
                .program(dto.getProgram())
                .currentSemester(dto.getCurrentSemester())
                .permanentAddress(dto.getPermanentAddress())
                .presentAddress(dto.getPresentAddress())
                .isActive(dto.getIsActive() != null ? dto.getIsActive() : true)
                .build();

        // Set relationships if IDs are provided
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + dto.getDepartmentId()));
            student.setDepartment(department);
        }

        if (dto.getClassId() != null) {
            Classes classEntity = classesRepository.findById(dto.getClassId())
                    .orElseThrow(() -> new EntityNotFoundException("Class not found with id: " + dto.getClassId()));
            student.setClassEntity(classEntity);
        }

        if (dto.getSectionId() != null) {
            Section section = sectionRepository.findById(dto.getSectionId())
                    .orElseThrow(() -> new EntityNotFoundException("Section not found with id: " + dto.getSectionId()));
            student.setSection(section);
        }

        if (dto.getCourseIds() != null && !dto.getCourseIds().isEmpty()) {
            List<Course> courses = courseRepository.findAllById(dto.getCourseIds());
            student.setCourses(courses);
        }

        return student;
    }

    private void updateEntityFromDTO(Student student, StudentDTO dto) {
        if (dto.getStudentId() != null) student.setStudentId(dto.getStudentId());
        if (dto.getName() != null) student.setName(dto.getName());
        if (dto.getEmail() != null) student.setEmail(dto.getEmail());
        if (dto.getPhone() != null) student.setPhone(dto.getPhone());
        if (dto.getDob() != null) student.setDob(dto.getDob());
        if (dto.getGender() != null) student.setGender(dto.getGender());
        if (dto.getProgram() != null) student.setProgram(dto.getProgram());
        student.setCurrentSemester(dto.getCurrentSemester());
        if (dto.getPermanentAddress() != null) student.setPermanentAddress(dto.getPermanentAddress());
        if (dto.getPresentAddress() != null) student.setPresentAddress(dto.getPresentAddress());
        if (dto.getIsActive() != null) student.setIsActive(dto.getIsActive());

        // Update relationships if provided
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new EntityNotFoundException("Department not found with id: " + dto.getDepartmentId()));
            student.setDepartment(department);
        }

        if (dto.getClassId() != null) {
            Classes classEntity = classesRepository.findById(dto.getClassId())
                    .orElseThrow(() -> new EntityNotFoundException("Class not found with id: " + dto.getClassId()));
            student.setClassEntity(classEntity);
        }

        if (dto.getSectionId() != null) {
            Section section = sectionRepository.findById(dto.getSectionId())
                    .orElseThrow(() -> new EntityNotFoundException("Section not found with id: " + dto.getSectionId()));
            student.setSection(section);
        }
    }

    public StudentResponseDTO convertToResponseDTO(Student student) {
        return StudentResponseDTO.builder()
                .id(student.getId())
                .studentId(student.getStudentId())
                .name(student.getName())
                .email(student.getEmail())
                .phone(student.getPhone())
                .dob(student.getDob())
                .gender(student.getGender())
                .program(student.getProgram())
                .currentSemester(student.getCurrentSemester())
                .permanentAddress(student.getPermanentAddress())
                .presentAddress(student.getPresentAddress())
                .isActive(student.getIsActive())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .departmentName(student.getDepartment() != null ? student.getDepartment().getDeptName() : null)
                .className(student.getClassEntity() != null ? student.getClassEntity().getClassName() : null)
                .sectionName(student.getSection() != null ? student.getSection().getSectionName() : null)
                .courseNames(student.getCourses().stream()
                        .map(Course::getCourseName)
                        .collect(Collectors.toList()))
                .build();
    }
}