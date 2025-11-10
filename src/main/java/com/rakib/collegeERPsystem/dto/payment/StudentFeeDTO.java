package com.rakib.collegeERPsystem.dto.payment;


import lombok.*;
import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentFeeDTO {
    private Long id;
    private Long studentId;
    private String semenster;
    private String studentName;
    private Long feeStructureId;
    private String feeStructureName;
    private Double totalAmount;
    private Double paidAmount;
    private Double remainingAmount;
    private LocalDate dueDate;
    private String status; // e.g. PENDING, PAID, PARTIALLY_PAID

    private List<FeePaymentDTO> feePayments;
}

