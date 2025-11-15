package com.rakib.collegeERPsystem.controller;

import com.rakib.collegeERPsystem.dto.CourseDTO;
import com.rakib.collegeERPsystem.dto.StudentDTO;
import com.rakib.collegeERPsystem.dto.StudentResponseDTO;
import com.rakib.collegeERPsystem.entity.Student;
import com.rakib.collegeERPsystem.repository.StudentRepository;
import com.rakib.collegeERPsystem.service.CourseService;
import com.rakib.collegeERPsystem.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/courses")
@RequiredArgsConstructor
public class CourseController {

    private final CourseService courseService;
    private final StudentRepository studentRepository;
    private final StudentService studentService;


    // Get all courses
    @GetMapping
    public ResponseEntity<List<CourseDTO>> getAllCourses() {
        List<CourseDTO> courses = courseService.getAllCourses();
        return ResponseEntity.ok(courses);
    }

    // Get course by ID
    @GetMapping("/{id}")
    public ResponseEntity<CourseDTO> getCourseById(@PathVariable Long id) {
        CourseDTO course = courseService.getCourseById(id);
        return ResponseEntity.ok(course);
    }

    // Get course by course code
    @GetMapping("/code/{courseCode}")
    public ResponseEntity<CourseDTO> getCourseByCode(@PathVariable String courseCode) {
        CourseDTO course = courseService.getCourseByCode(courseCode);
        return ResponseEntity.ok(course);
    }

    // Create new course
    @PostMapping
    public ResponseEntity<CourseDTO> createCourse(@RequestBody CourseDTO courseDTO) {
        CourseDTO savedCourse = courseService.saveCourse(courseDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedCourse);
    }

    // Update existing course
    @PutMapping("/{id}")
    public ResponseEntity<CourseDTO> updateCourse(@PathVariable Long id, @RequestBody CourseDTO courseDTO) {
        courseDTO.setId(id);
        CourseDTO updatedCourse = courseService.saveCourse(courseDTO);
        return ResponseEntity.ok(updatedCourse);
    }

    // Delete course
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return ResponseEntity.noContent().build();
    }

    // Get courses by department ID
    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<CourseDTO>> getCoursesByDepartment(@PathVariable Long departmentId) {
        List<CourseDTO> courses = courseService.getCoursesByDepartmentId(departmentId);
        return ResponseEntity.ok(courses);
    }

    // Get courses by faculty ID
    @GetMapping("/faculty/{facultyId}")
    public ResponseEntity<List<CourseDTO>> getCoursesByFaculty(@PathVariable Long facultyId) {
        List<CourseDTO> courses = courseService.getCoursesByFacultyId(facultyId);
        return ResponseEntity.ok(courses);
    }

    // Get courses by student ID
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<CourseDTO>> getCoursesByStudent(@PathVariable Long studentId) {
        List<CourseDTO> courses = courseService.getCoursesByStudentId(studentId);
        return ResponseEntity.ok(courses);
    }

    // Check if course code exists
    @GetMapping("/exists/{courseCode}")
    public ResponseEntity<Boolean> checkCourseCodeExists(@PathVariable String courseCode) {
        // Use repository method instead of streaming all courses for better performance
        boolean exists = courseService.getAllCourses().stream()
                .anyMatch(course -> course.getCourseCode().equals(courseCode));
        return ResponseEntity.ok(exists);
    }

    // Add faculty to course
    @PostMapping("/{courseId}/faculties/{facultyId}")
    public ResponseEntity<CourseDTO> addFacultyToCourse(
            @PathVariable Long courseId,
            @PathVariable Long facultyId) {
        CourseDTO updatedCourse = courseService.addFacultyToCourse(courseId, facultyId);
        return ResponseEntity.ok(updatedCourse);
    }

    // Remove faculty from course
    @DeleteMapping("/{courseId}/faculties/{facultyId}")
    public ResponseEntity<CourseDTO> removeFacultyFromCourse(
            @PathVariable Long courseId,
            @PathVariable Long facultyId) {
        CourseDTO updatedCourse = courseService.removeFacultyFromCourse(courseId, facultyId);
        return ResponseEntity.ok(updatedCourse);
    }

    // Update all faculties for a course
    @PutMapping("/{courseId}/faculties")
    public ResponseEntity<CourseDTO> updateCourseFaculties(
            @PathVariable Long courseId,
            @RequestBody List<Long> facultyIds) {
        CourseDTO updatedCourse = courseService.updateCourseFaculties(courseId, facultyIds);
        return ResponseEntity.ok(updatedCourse);
    }
    @GetMapping("/{courseId}/students")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsByCourse(@PathVariable Long courseId) {
        try {
            List<Student> students = studentRepository.findByCourseId(courseId);

          List<StudentResponseDTO> stu = students.stream().map(student -> studentService.convertToResponseDTO(student)).toList();




            return ResponseEntity.ok(stu);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}