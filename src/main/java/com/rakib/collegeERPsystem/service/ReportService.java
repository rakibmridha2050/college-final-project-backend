package com.rakib.collegeERPsystem.service;

import com.rakib.collegeERPsystem.entity.exam.ExamResult;
import com.rakib.collegeERPsystem.entity.payment2.*;
import com.rakib.collegeERPsystem.repository.exam.ExamResultRepository;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReportService {
    private final PaymentRepository paymentRepository;
    private final ExamService examService;
    private final ExamResultRepository examResultRepository;



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


    public byte[] generateExamResultsPdf(String collegeName, List<Map<String, Object>> results) throws JRException {
        InputStream reportStream = this.getClass().getResourceAsStream("/reports/ResultSheetProfessional.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(reportStream);

        Map<String, Object> parameters = new HashMap<>();
        parameters.put("CollegeName", collegeName);
        parameters.put("GeneratedDate", new java.util.Date());

        JRBeanCollectionDataSource dataSource = new JRBeanCollectionDataSource(results);
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport, parameters, dataSource);

        return JasperExportManager.exportReportToPdf(jasperPrint);
    }

    public List<Map<String, Object>> getExamResultsByStudentId(Long studentId) {
        // Fetch results for a specific student
        List<ExamResult> results = examResultRepository.findByStudentId(studentId);

        // Map to Jasper-compatible structure
        return results.stream().map(er -> {
            Map<String, Object> map = new HashMap<>();
            map.put("student_id", er.getStudent().getStudentId());
            map.put("name", er.getStudent().getName());
            map.put("program", er.getStudent().getProgram());
            map.put("dept_name", er.getStudent().getDepartment().getDeptName());
            map.put("dept_code", er.getStudent().getDepartment().getDeptCode());
            map.put("exam_title", er.getExam().getExamTitle());
            map.put("total_marks", er.getExam().getTotalMarks());
            map.put("gender", er.getStudent().getGender());
            map.put("email", er.getStudent().getEmail());
            map.put("current_semester", er.getStudent().getCurrentSemester());
            map.put("marks_obtained", er.getMarksObtained());
            map.put("grade", er.getGrade());
            map.put("gpa", er.getGpa());
            map.put("remarks", er.getRemarks());
            map.put("status", er.getStatus().toString());
            map.put("course_name", er.getExam().getCourse().getCourseName());
            return map;
        }).toList();
    }




}
