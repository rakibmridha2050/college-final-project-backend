package com.rakib.collegeERPsystem.entity;



import com.rakib.collegeERPsystem.enums.AttendanceStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
@Entity
@Table(name = "attendance", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"student_id", "course_id", "attendance_date", "period_number"})
})
@Data
public class Attendance {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "course_id", nullable = false)
    private Course course;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id", nullable = false)
    private Section section;

    @Column(nullable = false)
    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AttendanceStatus status = AttendanceStatus.PRESENT ;

    private String remarks;

    private String recordedBy;

    private Integer periodNumber;

    // Constructors
    public Attendance() {}

    public Attendance(Student student, Course course, Section section, LocalDate attendanceDate,
                      AttendanceStatus status, String remarks, String recordedBy, Integer periodNumber) {
        this.student = student;
        this.course = course;
        this.section = section;
        this.attendanceDate = attendanceDate;
        this.status = status;
        this.remarks = remarks;
        this.recordedBy = recordedBy;
        this.periodNumber = periodNumber;
    }


}