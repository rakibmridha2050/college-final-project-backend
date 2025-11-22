package com.rakib.collegeERPsystem.controller;
import com.rakib.collegeERPsystem.dto.FacultyDTO;
import com.rakib.collegeERPsystem.service.FacultyService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
@RestController
@RequestMapping("/api/faculty")
@RequiredArgsConstructor
public class FacultyController {

    private final FacultyService facultyService;

    @GetMapping
    public ResponseEntity<List<FacultyDTO>> getAllFaculty() {
        return ResponseEntity.ok(facultyService.getAllFaculty());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FacultyDTO> getFacultyById(@PathVariable Long id) {
        return ResponseEntity.ok(facultyService.getFacultyById(id));
    }

    @PostMapping
    public ResponseEntity<FacultyDTO> saveFaculty(@RequestBody FacultyDTO facultyDTO) {
        return ResponseEntity.ok(facultyService.saveFaculty(facultyDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteFaculty(@PathVariable Long id) {
        facultyService.deleteFaculty(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/department/{deptId}")
    public ResponseEntity<List<FacultyDTO>> getFacultyByDepartment(@PathVariable Long deptId) {
        return ResponseEntity.ok(facultyService.getFacultyByDepartmentId(deptId));
    }

    @GetMapping("/count")
    public long getFacultyCount() {
        return facultyService.countFaculty();
    }
}