package com.rakib.collegeERPsystem.service;


import com.rakib.collegeERPsystem.dto.AttendanceFilterRequest;
import com.rakib.collegeERPsystem.dto.AttendanceRequestDTO;
import com.rakib.collegeERPsystem.dto.BulkAttendanceRequest;
import com.rakib.collegeERPsystem.dto.StudentAttendanceDTO;
import com.rakib.collegeERPsystem.entity.*;

import com.rakib.collegeERPsystem.enums.AttendanceStatus;
import com.rakib.collegeERPsystem.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AttendanceService {

    private final StudentRepository studentRepository;
    private final AttendanceRepository attendanceRepository;
    private final DepartmentRepository departmentRepository;
    private final ClassesRepository classesRepository;
    private final SectionRepository sectionRepository;
    private final CourseRepository courseRepository;



    // Get students for attendance marking
    public List<StudentAttendanceDTO> getStudentsForAttendance(AttendanceFilterRequest filter) {
        // Validate the hierarchy
        validateDepartmentClassSectionHierarchy(filter.getDepartmentId(), filter.getClassId(), filter.getSectionId());

        // Get students for the section
        List<Student> students = studentRepository.findBySectionIdAndCourseEnrollment(
                filter.getSectionId(), filter.getCourseId());

        // Get existing attendance records for the date and period
        List<Attendance> existingAttendance = attendanceRepository.findBySectionIdAndCourseIdAndAttendanceDateAndPeriodNumber(
                filter.getSectionId(), filter.getCourseId(), filter.getAttendanceDate(), filter.getPeriodNumber());

        Map<Long, Attendance> attendanceMap = existingAttendance.stream()
                .collect(Collectors.toMap(att -> att.getStudent().getId(), Function.identity()));

        // Convert to DTO
        return students.stream()
                .map(student -> {
                    StudentAttendanceDTO dto = new StudentAttendanceDTO();
                    dto.setStudentId(student.getId());
                    dto.setStudentRoll(student.getStudentId());
                    dto.setStudentName(student.getName());
                    dto.setEmail(student.getEmail());

                    // Set existing status if available
                    Attendance existing = attendanceMap.get(student.getId());
                    if (existing != null) {
                        dto.setStatus(existing.getStatus());
                        dto.setRemarks(existing.getRemarks());
                    } else {
                        dto.setStatus(AttendanceStatus.NOT_TAKEN_YET);
                    }

                    return dto;
                })
                .collect(Collectors.toList());
    }

    // Record bulk attendance
    public void recordBulkAttendance(BulkAttendanceRequest request) {
        // Validate if attendance already recorded for this section, course, date and period
        if (attendanceRepository.existsBySectionIdAndCourseIdAndAttendanceDateAndPeriodNumber(
                request.getSectionId(), request.getCourseId(), request.getAttendanceDate(), request.getPeriodNumber())) {
            throw new RuntimeException("Attendance already recorded for this section, course, date and period");
        }

        for (StudentAttendanceDTO record : request.getAttendanceRecords()) {
            Attendance attendance = new Attendance();

            Student student = studentRepository.findById(record.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found: " + record.getStudentId()));

            Course course = courseRepository.findById(request.getCourseId())
                    .orElseThrow(() -> new RuntimeException("Course not found: " + request.getCourseId()));

            Section section = sectionRepository.findById(request.getSectionId())
                    .orElseThrow(() -> new RuntimeException("Section not found: " + request.getSectionId()));

            attendance.setStudent(student);
            attendance.setCourse(course);
            attendance.setSection(section);
            attendance.setAttendanceDate(request.getAttendanceDate());
            attendance.setPeriodNumber(request.getPeriodNumber());
            attendance.setStatus(record.getStatus());
            attendance.setRemarks(record.getRemarks());
            attendance.setRecordedBy(request.getRecordedBy());

            attendanceRepository.save(attendance);
        }
    }

    // Update existing attendance
    public void updateAttendance(List<StudentAttendanceDTO> attendanceRecords,
                                 Long courseId, Long sectionId,
                                 LocalDate attendanceDate, Integer periodNumber) {

        for (StudentAttendanceDTO record : attendanceRecords) {
            Attendance attendance = attendanceRepository
                    .findByStudentIdAndCourseIdAndSectionIdAndAttendanceDateAndPeriodNumber(
                            record.getStudentId(), courseId, sectionId, attendanceDate, periodNumber)
                    .orElseThrow(() -> new RuntimeException("Attendance record not found for student: " + record.getStudentId()));

            attendance.setStatus(record.getStatus());
            attendance.setRemarks(record.getRemarks());

            attendanceRepository.save(attendance);
        }
    }

    private void validateDepartmentClassSectionHierarchy(Long departmentId, Long classId, Long sectionId) {
        // Validate department exists
        Department department = departmentRepository.findById(departmentId)
                .orElseThrow(() -> new RuntimeException("Department not found"));

        // Validate class exists and belongs to department
        Classes classes = classesRepository.findById(classId)
                .orElseThrow(() -> new RuntimeException("Class not found"));

        if (!classes.getDepartment().getId().equals(departmentId)) {
            throw new RuntimeException("Class does not belong to the selected department");
        }

        // Validate section exists and belongs to class
        Section section = sectionRepository.findById(sectionId)
                .orElseThrow(() -> new RuntimeException("Section not found"));

        if (!section.getClasses().getId().equals(classId)) {
            throw new RuntimeException("Section does not belong to the selected class");
        }
    }
}