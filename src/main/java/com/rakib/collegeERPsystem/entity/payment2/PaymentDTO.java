package com.rakib.collegeERPsystem.entity.payment2;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PaymentDTO {
    private Long id;
    private Long studentId;
    private Double amount;
    private String paymentMode;
    private String status;
    private LocalDateTime paymentDate;
    private String receiptNo;
    private String remarks;


}
