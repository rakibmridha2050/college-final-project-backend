package com.rakib.collegeERPsystem.entity.exam;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.rakib.collegeERPsystem.entity.BaseEntity;
import com.rakib.collegeERPsystem.entity.Course;
import com.rakib.collegeERPsystem.entity.Faculty;
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "exams")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Exam extends BaseEntity {

    @Column(nullable = false)
    private String examTitle;

    @Column(columnDefinition = "TEXT")
    private String description;

    @Column(nullable = false)
    private LocalDateTime examDate;

    @Column(nullable = false)
    private Integer duration; // in minutes

    @Column(nullable = false)
    private Double totalMarks;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ExamType examType; // MIDTERM, FINAL, QUIZ, ASSIGNMENT

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "faculty_id", nullable = false)
    private Faculty faculty;

    @OneToMany(mappedBy = "exam", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ExamResult> examResults = new ArrayList<>();

    @Column(nullable = false)
    private Boolean isPublished = false;
}

