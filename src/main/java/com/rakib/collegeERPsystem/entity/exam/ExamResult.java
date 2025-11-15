package com.rakib.collegeERPsystem.entity.exam;

import com.rakib.collegeERPsystem.entity.BaseEntity;
import com.rakib.collegeERPsystem.entity.Faculty;
import com.rakib.collegeERPsystem.entity.Student;
import jakarta.persistence.*;
import lombok.*;



@Entity
@Table(name = "exam_results")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ExamResult extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exam_id", nullable = false)
    private Exam exam;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @Column(nullable = false)
    private Double marksObtained;

    private String grade;


    private Double gpa;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ResultStatus status = ResultStatus.PENDING;

    @Column(columnDefinition = "TEXT")
    private String remarks;

    // Unique constraint to prevent duplicate entries
//    @TableUnique(columns = {"exam_id", "student_id"})
    public static class UniqueConstraint {}
}

