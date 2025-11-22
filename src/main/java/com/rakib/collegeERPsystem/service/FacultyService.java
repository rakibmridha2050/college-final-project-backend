package com.rakib.collegeERPsystem.service;
import com.rakib.collegeERPsystem.dto.FacultyDTO;
import com.rakib.collegeERPsystem.entity.Department;
import com.rakib.collegeERPsystem.entity.Faculty;
import com.rakib.collegeERPsystem.entity.User;
import com.rakib.collegeERPsystem.repository.DepartmentRepository;
import com.rakib.collegeERPsystem.repository.FacultyRepository;
import com.rakib.collegeERPsystem.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class FacultyService {

    private final FacultyRepository facultyRepository;
    private final DepartmentRepository departmentRepository;
    private final UserRepository userRepository;


    public List<FacultyDTO> getAllFaculty() {
        return facultyRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public FacultyDTO getFacultyById(Long id) {
        Faculty faculty = facultyRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + id));
        return mapToDTO(faculty);
    }



//    public FacultyDTO saveFaculty(FacultyDTO dto) {
//        Faculty faculty = mapToEntity(dto);
//        Faculty saved = facultyRepository.save(faculty);
//        return mapToDTO(saved);
//    }



//    public void deleteFaculty(Long id) {
//        facultyRepository.deleteById(id);
//    }


    public FacultyDTO saveFaculty(FacultyDTO dto) {
        Faculty faculty;

        // Handle update scenario properly
        if (dto.getId() != null) {
            // For update, get the existing entity first
            faculty = facultyRepository.findById(dto.getId())
                    .orElseThrow(() -> new RuntimeException("Faculty not found with id: " + dto.getId()));

            // Update the fields
            faculty.setName(dto.getName());
            faculty.setEmail(dto.getEmail());
            faculty.setPhone(dto.getPhone());
            faculty.setDesignation(dto.getDesignation());
        } else {
            // For create, use the builder
            faculty = Faculty.builder()
                    .name(dto.getName())
                    .email(dto.getEmail())
                    .phone(dto.getPhone())
                    .designation(dto.getDesignation())
                    .build();
        }

        // Set User - REQUIRED since user_id is NOT NULL in database
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));
            faculty.setUser(user);
        } else {
            throw new RuntimeException("User ID is required for Faculty");
        }

        // Set Department
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + dto.getDepartmentId()));
            faculty.setDepartment(department);
        } else {
            throw new RuntimeException("Department ID is required for Faculty");
        }

        Faculty saved = facultyRepository.save(faculty);
        return mapToDTO(saved);
    }

    public void deleteFaculty(Long id) {
        facultyRepository.deleteById(id);
    }

    // Get faculty by department ID
    public List<FacultyDTO> getFacultyByDepartmentId(Long departmentId) {
        return facultyRepository.findByDepartmentId(departmentId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    // Get faculty by department ID
//    public List<FacultyDTO> getFacultyByDepartmentId(Long departmentId) {
//        return facultyRepository.findByDepartmentId(departmentId)
//                .stream()
//                .map(this::mapToDTO)
//                .collect(Collectors.toList());
//    }

    private Faculty mapToEntity(FacultyDTO dto) {
        Faculty faculty = Faculty.builder()
                .name(dto.getName())
                .email(dto.getEmail())
                .phone(dto.getPhone())
                .designation(dto.getDesignation())
                .build();

        if (dto.getId() != null) {
            faculty.setId(dto.getId());
        }

        // Set User - REQUIRED since user_id is NOT NULL in database
        if (dto.getUserId() != null) {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getUserId()));
            faculty.setUser(user);
        } else {
            throw new RuntimeException("User ID is required for Faculty");
        }

        // Set Department
        if (dto.getDepartmentId() != null) {
            Department department = departmentRepository.findById(dto.getDepartmentId())
                    .orElseThrow(() -> new RuntimeException("Department not found with id: " + dto.getDepartmentId()));
            faculty.setDepartment(department);
        } else {
            throw new RuntimeException("Department ID is required for Faculty");
        }

        return faculty;
    }

    private FacultyDTO mapToDTO(Faculty faculty) {
        return FacultyDTO.builder()
                .id(faculty.getId())
                .name(faculty.getName())
                .email(faculty.getEmail())
                .phone(faculty.getPhone())
                .designation(faculty.getDesignation())
                .userId(faculty.getUser() != null ? faculty.getUser().getId() : null)
                .userName(faculty.getUser() != null ? faculty.getUser().getUsername() : null)
                .departmentId(faculty.getDepartment() != null ? faculty.getDepartment().getId() : null)
                .departmentName(faculty.getDepartment() != null ? faculty.getDepartment().getDeptName() : null)
                .build();
    }

    public long countFaculty() {
        return facultyRepository.count();
    }
}