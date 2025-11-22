package com.rakib.collegeERPsystem.repository;

import com.rakib.collegeERPsystem.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Long> {

    Optional<Student> findByStudentId(String studentId);

    Optional<Student> findByEmail(String email);

    List<Student> findByNameContainingIgnoreCase(String name);

    List<Student> findByProgram(String program);

    List<Student> findByCurrentSemester(int semester);

    List<Student> findByDepartmentId(Long departmentId);

    List<Student> findByClassEntityId(Long classId);

    List<Student> findBySectionId(Long sectionId);

    List<Student> findByIsActiveTrue();

    List<Student> findByIsActiveFalse();

    @Query("SELECT s FROM Student s WHERE s.department.id = :deptId AND s.currentSemester = :semester")
    List<Student> findByDepartmentAndSemester(@Param("deptId") Long deptId, @Param("semester") int semester);

    boolean existsByStudentId(String studentId);

    boolean existsByEmail(String email);

    @Query("SELECT COUNT(s) FROM Student s WHERE s.department.id = :departmentId")
    Long countByDepartmentId(@Param("departmentId") Long departmentId);


    // Add this method to find students by course
    @Query("SELECT s FROM Student s JOIN s.courses c WHERE c.id = :courseId")
    List<Student> findByCourseId(@Param("courseId") Long courseId);

    // Or using the relationship directly if you have it mapped
    List<Student> findByCoursesId(Long courseId);

    //new for attendance
    // Find students by section
    List<Student> findBySectionIdAndIsActiveTrue(Long sectionId);

    // Find students by section and course enrollment
    @Query("SELECT s FROM Student s JOIN s.courses c WHERE s.section.id = :sectionId AND c.id = :courseId AND s.isActive = true")
    List<Student> findBySectionIdAndCourseEnrollment(@Param("sectionId") Long sectionId, @Param("courseId") Long courseId);

    long count();
}