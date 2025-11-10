package com.rakib.collegeERPsystem.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.rakib.collegeERPsystem.entity.payment.FeeWaiver;
import com.rakib.collegeERPsystem.entity.payment.StudentFee;
import com.rakib.collegeERPsystem.entity.payment2.Payment;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
public class Student extends BaseEntity {



//    @NotBlank
//    @Column(name = "student_id", unique = true, nullable = false, length = 20)
    private String studentId; // Unique university roll, e.g. CSE-2025-001

//    @NotBlank
    private String name;

//    @Email
//    @NotBlank
//    @Column(unique = true, nullable = false)
    private String email;

//    @Pattern(regexp = "^(\\+88)?01[3-9]\\d{8}$", message = "Invalid Bangladeshi mobile number")
//    @NotBlank
    private String phone;

    private LocalDate dob;

    private String gender;

//    @NotBlank
    private String program; // e.g. BSc in CSE

    @ManyToMany
    @JoinTable(
            name = "course_student",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id")
    )
    private List<Course> courses = new ArrayList<>();

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "dept_id")
    private Department department;

//    @Min(1)
    private int currentSemester;

    @Column(name = "permanent_address", columnDefinition = "TEXT")
    private String permanentAddress;

    @Column(name = "present_address", columnDefinition = "TEXT")
    private String presentAddress;

    private Boolean isActive = true;

    // 🔗 Many Students belong to one Class
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "class_id")
    private Classes classEntity;

    // 🔗 Many Students belong to one Section
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "section_id")
    private Section section;

    // 🔗 One Student can have many Fee Invoices
//    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<StudentFee> studentFees;

    // 🔗 One Student can have multiple Fee Waivers
    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeeWaiver> feeWaivers;


    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Payment> payments;
}
