package com.rakib.collegeERPsystem.entity.payment;



import com.rakib.collegeERPsystem.entity.BaseEntity;
import com.rakib.collegeERPsystem.enums.FeeStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import java.util.List;

@Entity
@Table(name = "fee_structures")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeeStructure extends BaseEntity {



    @Column(nullable = false)
    @NotBlank(message = "Program name is required")
    private String program;

    @Column(nullable = false)
    @Min(value = 1)
    private int semester;

    @Column(name = "student_category", nullable = false)
    @NotBlank(message = "Student category is required")
    private String studentCategory;

    @Column(name = "academic_session", nullable = false)
    @NotBlank(message = "Academic session is required")
    private String academicSession; // e.g. 2024-2025

    @Column(name = "total_amount", nullable = false)
    @PositiveOrZero
    private Double totalAmount;

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private FeeStatus status = FeeStatus.ACTIVE;

    // 🔗 One-to-Many with FeeComponent
    @OneToMany(mappedBy = "feeStructure", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeeComponent> feeComponents;

    // 🔗 One-to-Many with StudentFee
    @OneToMany(mappedBy = "feeStructure", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentFee> studentFees;
}

