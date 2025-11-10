package com.rakib.collegeERPsystem.entity.payment2;

import com.rakib.collegeERPsystem.entity.Student;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "payments")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "student_id", nullable = false)
    @ToString.Exclude
    private Student student;

    @Column(nullable = false)
    private Double amount;

    @Column(nullable = false)
    private String paymentMode; // e.g., CASH, CARD, UPI, BANK

    @Column(nullable = false)
    private String status; // e.g., SUCCESS, PENDING, FAILED

    @Column(nullable = false)
    private LocalDateTime paymentDate;

    private String receiptNo;

    private String remarks;
}