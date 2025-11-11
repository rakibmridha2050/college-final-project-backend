package com.rakib.collegeERPsystem.service;

import com.rakib.collegeERPsystem.entity.payment2.*;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final PaymentRepository paymentRepository;
    public JasperPrint getJasperPrint() throws IOException, JRException {

        InputStream reportStream = new ClassPathResource("reports/payments.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        List<Payment> payments = paymentRepository.findAll();

        List<PaymentReportDTO> paymentDTOS = payments.stream()
                .map(payment -> PaymentReportDTO.builder()
                        .paymentId(payment.getId())
                        .studentId(payment.getStudent().getStudentId())
                        .studentEmail(payment.getStudent().getEmail())
                        .studentName(payment.getStudent().getName())
                        .amount(payment.getAmount())
                        .paymentMode(payment.getPaymentMode())
                        .status(payment.getStatus())
                        .paymentDate(payment.getPaymentDate())
                        .receiptNo(payment.getReceiptNo())
                        .remarks(payment.getRemarks())
                        .build())
                .sorted((p1, p2) -> p1.getStudentId().compareTo(p2.getStudentId()))
                .collect(Collectors.toList());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(paymentDTOS);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Rakib");

        return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
    }

    public JasperPrint getSinglePayslip(Long paymentId) throws IOException, JRException {
        InputStream reportStream = new ClassPathResource("reports/payslip.jrxml").getInputStream();
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        Payment payment = paymentRepository.findById(paymentId)
                .orElseThrow(() -> new RuntimeException("Payment not found with id " + paymentId));

        PaymentDTO dto = PaymentDTO.builder()
                .id(payment.getId())
                .studentId(Long.valueOf(payment.getStudent().getStudentId()))
                .amount(payment.getAmount())
                .paymentMode(payment.getPaymentMode())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .receiptNo(payment.getReceiptNo())
                .remarks(payment.getRemarks())
                .build();

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(java.util.List.of(dto));
        Map<String, Object> parameters = new HashMap<>();
        parameters.put("createdBy", "Rakib");

        return JasperFillManager.fillReport(jasperReport, parameters, dataSource);
    }


}
