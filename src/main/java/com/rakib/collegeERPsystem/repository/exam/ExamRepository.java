package com.rakib.collegeERPsystem.repository.exam;

import com.rakib.collegeERPsystem.entity.exam.Exam;
import com.rakib.collegeERPsystem.entity.exam.ExamResult;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ExamRepository extends JpaRepository<Exam, Long> {

    List<Exam> findByFacultyIdOrderByExamDateDesc(Long facultyId);

    List<Exam> findByCourseIdOrderByExamDateDesc(Long courseId);

    List<Exam> findByFacultyIdAndExamDateBetween(Long facultyId, LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT e FROM Exam e WHERE e.course.id = :courseId AND e.examType = :examType")
    List<Exam> findByCourseIdAndExamType(@Param("courseId") Long courseId, @Param("examType") String examType);

    @Query("SELECT e FROM Exam e WHERE e.faculty.id = :facultyId AND e.isPublished = true")
    List<Exam> findPublishedExamsByFacultyId(@Param("facultyId") Long facultyId);

    boolean existsByCourseIdAndExamTitleAndExamDateBetween(Long courseId, String examTitle,
                                                           LocalDateTime startDate, LocalDateTime endDate);

    @Query("SELECT e FROM Exam e JOIN e.course c WHERE c.department.id = :departmentId")
    List<Exam> findByDepartmentId(@Param("departmentId") Long departmentId);

    Optional<Exam> findByIdAndFacultyId(Long id, Long facultyId);
}
