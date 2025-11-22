package com.rakib.collegeERPsystem.service;

import com.rakib.collegeERPsystem.dto.CourseDTO;
import com.rakib.collegeERPsystem.entity.Course;
import com.rakib.collegeERPsystem.entity.Department;
import com.rakib.collegeERPsystem.entity.Faculty;
import com.rakib.collegeERPsystem.repository.CourseRepository;
import com.rakib.collegeERPsystem.repository.DepartmentRepository;
import com.rakib.collegeERPsystem.repository.FacultyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CourseService {

    private final CourseRepository courseRepository;
    private final DepartmentRepository departmentRepository;
    private final FacultyRepository facultyRepository; // Added this

    // Get all courses
    @Transactional(readOnly = true)
    public List<CourseDTO> getAllCourses() {
        return courseRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get course by ID
    @Transactional(readOnly = true)
    public CourseDTO getCourseById(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + id));
        return mapToDTO(course);
    }

    // Get course by course code
    @Transactional(readOnly = true)
    public CourseDTO getCourseByCode(String courseCode) {
        Course course = courseRepository.findByCourseCode(courseCode)
                .orElseThrow(() -> new RuntimeException("Course not found with code: " + courseCode));
        return mapToDTO(course);
    }

    // Create or update course
    public CourseDTO saveCourse(CourseDTO dto) {
        // Validate course code uniqueness for new courses
        if (dto.getId() == null && courseRepository.existsByCourseCode(dto.getCourseCode())) {
            throw new RuntimeException("Course code already exists: " + dto.getCourseCode());
        }

        // Validate course code uniqueness for updates
        if (dto.getId() != null && courseRepository.existsByCourseCodeAndIdNot(dto.getCourseCode(), dto.getId())) {
            throw new RuntimeException("Course code already exists: " + dto.getCourseCode());
        }

        Course course;

        if (dto.getId() != null) {
            // Update existing course
            course = courseRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Course not found with id: " + dto.getId()));

            // Update fields
            course.setCourseCode(dto.getCourseCode());
            course.setCourseName(dto.getCourseName());
            course.setCredits(dto.getCredits());
        } else {
            // Create new course
            course = Course.builder()
                    .courseCode(dto.getCourseCode())
                    .courseName(dto.getCourseName())
                    .credits(dto.getCredits())
                    .build();
        }

        // Set Department
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + dto.getDepartmentId()));
            course.setDepartment(department);
        } else {
            throw new RuntimeException("Department ID is required for Course");
        }

        Course saved = courseRepository.save(course);
        return mapToDTO(saved);
    }

    // Delete course
    public void deleteCourse(Long id) {
        if (!courseRepository.existsById(id)) {
            throw new RuntimeException("Course not found with id: " + id);
        }
        courseRepository.deleteById(id);
    }

    // Get courses by department ID
    @Transactional(readOnly = true)
    public List<CourseDTO> getCoursesByDepartmentId(Long departmentId) {
        return courseRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get courses by faculty ID
    @Transactional(readOnly = true)
    public List<CourseDTO> getCoursesByFacultyId(Long facultyId) {
        return courseRepository.findByFacultyId(facultyId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get courses by student ID
    @Transactional(readOnly = true)
    public List<CourseDTO> getCoursesByStudentId(Long studentId) {
        return courseRepository.findByStudentId(studentId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Add faculty to course
    public CourseDTO addFacultyToCourse(Long courseId, Long facultyId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        Faculty faculty = facultyRepository.findById(facultyId)
                .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + facultyId));

        // Initialize the faculties list if null
        if (course.getFaculties() == null) {
            course.setFaculties(new ArrayList<>());
        }

        // Add faculty if not already present
        boolean facultyExists = course.getFaculties().stream()
                .anyMatch(f -> f.getId().equals(facultyId));

        if (!facultyExists) {
            course.getFaculties().add(faculty);
        }

        Course saved = courseRepository.save(course);
        return mapToDTO(saved);
    }

    // Remove faculty from course
    public CourseDTO removeFacultyFromCourse(Long courseId, Long facultyId) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        if (course.getFaculties() != null) {
            course.getFaculties().removeIf(faculty -> faculty.getId().equals(facultyId));
        }

        Course saved = courseRepository.save(course);
        return mapToDTO(saved);
    }

    // Update all faculties for a course
    public CourseDTO updateCourseFaculties(Long courseId, List<Long> facultyIds) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found with id: " + courseId));

        // Clear existing faculties
        if (course.getFaculties() != null) {
            course.getFaculties().clear();
        } else {
            course.setFaculties(new ArrayList<>());
        }

        // Add new faculties
        for (Long facultyId : facultyIds) {
            Faculty faculty = facultyRepository.findById(facultyId)
                    .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + facultyId));
            course.getFaculties().add(faculty);
        }

        Course saved = courseRepository.save(course);
        return mapToDTO(saved);
    }

    public long countCourses() {
        return courseRepository.count();
    }

    // Map entity to DTO
    private CourseDTO mapToDTO(Course course) {
        return CourseDTO.builder()
                .id(course.getId())
                .courseCode(course.getCourseCode())
                .courseName(course.getCourseName())
                .credits(course.getCredits())
                .departmentId(course.getDepartment() != null ? course.getDepartment().getId() : null)
                .departmentName(course.getDepartment() != null ? course.getDepartment().getDeptName() : null)
                .subjectCount(course.getSubjects() != null ? course.getSubjects().size() : 0)
                .studentCount(course.getStudents() != null ? course.getStudents().size() : 0)
                .facultyCount(course.getFaculties() != null ? course.getFaculties().size() : 0)
                .semesterCount(course.getSemesters() != null ? course.getSemesters().size() : 0)
                .facultyIds(course.getFaculties() != null ?
                        course.getFaculties().stream().map(Faculty::getId).collect(Collectors.toList()) :
                        new ArrayList<>())
                .studentIds(course.getStudents() != null ?
                        course.getStudents().stream().map(s -> s.getId()).collect(Collectors.toList()) :
                        new ArrayList<>())
                .build();
    }
}