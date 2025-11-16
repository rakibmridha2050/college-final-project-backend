package com.rakib.collegeERPsystem.repository.exam;

import com.rakib.collegeERPsystem.dto.exam.StudentExamResultDTO;
import com.rakib.collegeERPsystem.entity.exam.Exam;
import com.rakib.collegeERPsystem.entity.exam.ExamResult;
import com.rakib.collegeERPsystem.entity.exam.ResultStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ExamResultRepository extends JpaRepository<ExamResult, Long> {

    List<ExamResult> findByExamId(Long examId);

    List<ExamResult> findByStudentId(Long studentId);

    List<ExamResult> findByExamIdAndStudentIdIn(Long examId, List<Long> studentIds);

    Optional<ExamResult> findByExamIdAndStudentId(Long examId, Long studentId);

    @Query("SELECT er FROM ExamResult er WHERE er.exam.course.id = :courseId AND er.student.id = :studentId")
    List<ExamResult> findByCourseIdAndStudentId(@Param("courseId") Long courseId, @Param("studentId") Long studentId);

    @Query("SELECT COUNT(er) FROM ExamResult er WHERE er.exam.id = :examId AND er.status = :status")
    Long countByExamIdAndStatus(@Param("examId") Long examId, @Param("status") ResultStatus status);

    @Query("SELECT AVG(er.marksObtained) FROM ExamResult er WHERE er.exam.id = :examId AND er.status != 'ABSENT'")
    Double findAverageMarksByExamId(@Param("examId") Long examId);

    @Query("SELECT MAX(er.marksObtained) FROM ExamResult er WHERE er.exam.id = :examId")
    Double findHighestMarksByExamId(@Param("examId") Long examId);

    @Query("SELECT MIN(er.marksObtained) FROM ExamResult er WHERE er.exam.id = :examId AND er.status != 'ABSENT'")
    Double findLowestMarksByExamId(@Param("examId") Long examId);

    boolean existsByExamIdAndStudentId(Long examId, Long studentId);

    @Query("SELECT er FROM ExamResult er WHERE er.exam.id = :examId ORDER BY er.marksObtained DESC")
    List<ExamResult> findByExamIdOrderByMarksObtainedDesc(@Param("examId") Long examId);


    @Query("SELECT er FROM ExamResult er " +
            "JOIN FETCH er.student s " +
            "JOIN FETCH s.department d " +
            "JOIN FETCH er.exam e " +
            "JOIN FETCH e.course co " +
            "ORDER BY s.studentId, e.examTitle, co.courseName")
    List<ExamResult> fetchAllStudentExamResults();


}