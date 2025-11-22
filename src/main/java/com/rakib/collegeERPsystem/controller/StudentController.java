package com.rakib.collegeERPsystem.controller;

import com.rakib.collegeERPsystem.dto.StudentDTO;
import com.rakib.collegeERPsystem.dto.StudentResponseDTO;
import com.rakib.collegeERPsystem.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @PostMapping
    public ResponseEntity<StudentResponseDTO> createStudent(@Valid @RequestBody StudentDTO studentDTO) {
        StudentResponseDTO createdStudent = studentService.createStudent(studentDTO);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    @GetMapping("/section/{sectionId}")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsBySectionId(@PathVariable Long sectionId) {
        List<StudentResponseDTO> students = studentService.getStudentBySectionId(sectionId);
        return ResponseEntity.ok(students);
    }

    @GetMapping
    public ResponseEntity<List<StudentResponseDTO>> getAllStudents() {
        List<StudentResponseDTO> students = studentService.getAllStudents();
        return ResponseEntity.ok(students);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> getStudentById(@PathVariable Long id) {
        StudentResponseDTO student = studentService.getStudentById(id);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/student-id/{studentId}")
    public ResponseEntity<StudentResponseDTO> getStudentByStudentId(@PathVariable String studentId) {
        StudentResponseDTO student = studentService.getStudentByStudentId(studentId);
        return ResponseEntity.ok(student);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentResponseDTO> updateStudent(
            @PathVariable Long id,
            @Valid @RequestBody StudentDTO studentDTO) {
        StudentResponseDTO updatedStudent = studentService.updateStudent(id, studentDTO);
        return ResponseEntity.ok(updatedStudent);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Long id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @PatchMapping("/{id}/soft-delete")
    public ResponseEntity<Void> softDeleteStudent(@PathVariable Long id) {
        studentService.softDeleteStudent(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/{departmentId}")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsByDepartment(@PathVariable Long departmentId) {
        List<StudentResponseDTO> students = studentService.getStudentsByDepartment(departmentId);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/program/{program}")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsByProgram(@PathVariable String program) {
        List<StudentResponseDTO> students = studentService.getStudentsByProgram(program);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/semester/{semester}")
    public ResponseEntity<List<StudentResponseDTO>> getStudentsBySemester(@PathVariable int semester) {
        List<StudentResponseDTO> students = studentService.getStudentsBySemester(semester);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/search")
    public ResponseEntity<List<StudentResponseDTO>> searchStudentsByName(@RequestParam String name) {
        List<StudentResponseDTO> students = studentService.searchStudentsByName(name);
        return ResponseEntity.ok(students);
    }

    @GetMapping("/active")
    public ResponseEntity<List<StudentResponseDTO>> getActiveStudents() {
        List<StudentResponseDTO> students = studentService.getActiveStudents();
        return ResponseEntity.ok(students);
    }

    @PostMapping("/{studentId}/courses")
    public ResponseEntity<StudentResponseDTO> addCoursesToStudent(
            @PathVariable Long studentId,
            @RequestBody List<Long> courseIds) {
        StudentResponseDTO student = studentService.addCoursesToStudent(studentId, courseIds);
        return ResponseEntity.ok(student);
    }

    @DeleteMapping("/{studentId}/courses")
    public ResponseEntity<StudentResponseDTO> removeCoursesFromStudent(
            @PathVariable Long studentId,
            @RequestBody List<Long> courseIds) {
        StudentResponseDTO student = studentService.removeCoursesFromStudent(studentId, courseIds);
        return ResponseEntity.ok(student);
    }

    @GetMapping("/count")
    public long getStudentCount() {
        return studentService.countStudents();
    }
}