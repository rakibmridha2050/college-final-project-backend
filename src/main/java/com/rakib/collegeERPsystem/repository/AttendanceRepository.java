package com.rakib.collegeERPsystem.repository;

import com.rakib.collegeERPsystem.entity.Attendance;
import com.rakib.collegeERPsystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Long> {


    // Find attendance by student, course, section, date and period
    Optional<Attendance> findByStudentIdAndCourseIdAndSectionIdAndAttendanceDateAndPeriodNumber(
            Long studentId, Long courseId, Long sectionId, LocalDate attendanceDate, Integer periodNumber);

    // Find all attendance records for a section on specific date and period
    List<Attendance> findBySectionIdAndCourseIdAndAttendanceDateAndPeriodNumber(
            Long sectionId, Long courseId, LocalDate attendanceDate, Integer periodNumber);

    // Check if attendance already recorded for section
    boolean existsBySectionIdAndCourseIdAndAttendanceDateAndPeriodNumber(
            Long sectionId, Long courseId, LocalDate attendanceDate, Integer periodNumber);
}


