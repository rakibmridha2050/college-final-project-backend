package com.rakib.collegeERPsystem.entity.payment2;

import lombok.Builder;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Builder
public class PaymentReportDTO {
    private Long paymentId;
    private String studentId;
    private String studentName;
    private String studentEmail;
    private Double amount;
    private String paymentMode;
    private String status;
    private LocalDateTime paymentDate;
    private String receiptNo;
    private String remarks;
}