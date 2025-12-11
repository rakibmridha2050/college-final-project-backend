package com.rakib.collegeERPsystem.entity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;


@Entity
@Table(name = "courses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Course extends BaseEntity{


    @Column(nullable = false, unique = true)
    private String courseCode;

    @Column(nullable = false)
    private String courseName;

    private Integer credits;



    // --- Many Courses belong to one Department ---
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id", nullable = false)
    private Department department;


    // --- One Course has many Subjects ---
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Subject> subjects = new ArrayList<>();

    // --- Many Students can enroll in a Course ---
    @ManyToMany(mappedBy = "courses")
    private List<Student> students = new ArrayList<>();

    // --- Many Faculties can teach a Course ---
//    @ManyToMany
//    @JoinTable(
//            name = "course_faculty",
//            joinColumns = @JoinColumn(name = "course_id"),
//            inverseJoinColumns = @JoinColumn(name = "faculty_id")
//    )
//    private List<Faculty> faculties = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "course_faculty",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "faculty_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"course_id", "faculty_id"})
    )
    private List<Faculty> faculties = new ArrayList<>();


    // --- One Course can have many Semesters ---
    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private List<Semester> semesters = new ArrayList<>();

    // helper methods
    public void addSubject(Subject subject) {
        subjects.add(subject);
        subject.setCourse(this);
    }
}
