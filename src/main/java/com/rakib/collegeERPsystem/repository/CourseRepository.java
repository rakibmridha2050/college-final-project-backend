package com.rakib.collegeERPsystem.repository;

import com.rakib.collegeERPsystem.entity.Course;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CourseRepository extends JpaRepository<Course, Long> {

    // Find course by course code
    Optional<Course> findByCourseCode(String courseCode);

    // Find all courses by department ID
    List<Course> findByDepartmentId(Long departmentId);

    // Find courses by faculty ID (through the many-to-many relationship)
    @Query("SELECT c FROM Course c JOIN c.faculties f WHERE f.id = :facultyId")
    List<Course> findByFacultyId(@Param("facultyId") Long facultyId);

    // Find courses by student ID (through the many-to-many relationship)
    @Query("SELECT c FROM Course c JOIN c.students s WHERE s.id = :studentId")
    List<Course> findByStudentId(@Param("studentId") Long studentId);

    // Check if course code exists (for validation)
    boolean existsByCourseCode(String courseCode);

    // Check if course code exists excluding a specific course (for update)
    boolean existsByCourseCodeAndIdNot(String courseCode, Long id);

    long count();
}