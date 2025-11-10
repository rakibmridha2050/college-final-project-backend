package com.rakib.collegeERPsystem.entity.payment2;

import com.rakib.collegeERPsystem.entity.Student;
import com.rakib.collegeERPsystem.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PaymentService {


    private final PaymentRepository paymentRepository;
    private final StudentRepository studentRepository;

    public PaymentDTO createPayment(PaymentDTO dto) {

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new IllegalArgumentException(
                        "Student not found with ID: " + dto.getStudentId()));



        Payment payment = Payment.builder()
                .student(student)
                .amount(dto.getAmount())
                .paymentMode(dto.getPaymentMode())
                .status(dto.getStatus() != null ? dto.getStatus() : "SUCCESS")
                .paymentDate(LocalDateTime.now())
                .receiptNo("RCPT-" + System.currentTimeMillis())
                .remarks(dto.getRemarks())
                .build();

        paymentRepository.save(payment);
        return mapToDTO(payment);
    }


    public PaymentDTO getPaymentById(Long id) {
        Payment payment = paymentRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Payment not found with id: " + id));
        return mapToDTO(payment);
    }


    public List<PaymentDTO> getPaymentsByStudent(Long studentId) {
        return paymentRepository.findByStudentId(studentId)
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public List<PaymentDTO> getAllPayments() {
        return paymentRepository.findAll()
                .stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deletePayment(Long id) {
        paymentRepository.deleteById(id);
    }

    private PaymentDTO mapToDTO(Payment payment) {
        return PaymentDTO.builder()
                .id(payment.getId())
                .studentId(payment.getStudent().getId())
                .amount(payment.getAmount())
                .paymentMode(payment.getPaymentMode())
                .status(payment.getStatus())
                .paymentDate(payment.getPaymentDate())
                .receiptNo(payment.getReceiptNo())
                .remarks(payment.getRemarks())
                .build();
    }
}
