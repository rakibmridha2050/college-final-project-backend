package com.rakib.collegeERPsystem.service.payment.impl;


import com.rakib.collegeERPsystem.dto.payment.StudentFeeDTO;
import com.rakib.collegeERPsystem.entity.Student;
import com.rakib.collegeERPsystem.entity.payment.FeeStructure;
import com.rakib.collegeERPsystem.entity.payment.StudentFee;
import com.rakib.collegeERPsystem.enums.FeeInvoiceStatus;
import com.rakib.collegeERPsystem.repository.StudentRepository;
import com.rakib.collegeERPsystem.repository.payment.FeePaymentRepository;
import com.rakib.collegeERPsystem.repository.payment.FeeStructureRepository;
import com.rakib.collegeERPsystem.repository.payment.StudentFeeRepository;
import com.rakib.collegeERPsystem.service.payment.StudentFeeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class StudentFeeServiceImpl implements StudentFeeService {

    private final StudentFeeRepository studentFeeRepository;
    private final StudentRepository studentRepository;
    private final FeeStructureRepository feeStructureRepository;
    private final FeePaymentRepository feePaymentRepository;

    @Override
    @Transactional
    public StudentFeeDTO generateFeeInvoice(StudentFeeDTO studentFeeDTO) {
        try {
            Student student = studentRepository.findById(studentFeeDTO.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found"));

            FeeStructure feeStructure = feeStructureRepository.findById(studentFeeDTO.getFeeStructureId())
                    .orElseThrow(() -> new RuntimeException("Fee structure not found"));

            // Check if invoice already exists for this student, semester, and session
//            if (studentFeeRepository.findByStudentAndSemesterAndSession(
//                    student.getId(), studentFeeDTO.getSemester(), studentFeeDTO.getAcademicSession()).isPresent()) {
//                throw new RuntimeException("Fee invoice already exists for this student in the given semester and session");
//            }

            String invoiceNumber = generateInvoiceNumber();

            StudentFee studentFee = StudentFee.builder()
                    .invoiceNumber(invoiceNumber)
                    .issueDate(LocalDate.now())
                    .dueDate(studentFeeDTO.getDueDate())
                    .totalAmount(feeStructure.getTotalAmount())
//                    .academicSession(studentFeeDTO.getAcademicSession())
//                    .semester(studentFeeDTO.getSemester())
                    .student(student)
                    .feeStructure(feeStructure)
                    .status(FeeInvoiceStatus.GENERATED)
                    .build();

            StudentFee savedInvoice = studentFeeRepository.save(studentFee);
            log.info("Fee invoice generated successfully: {}", invoiceNumber);

            return convertToDTO(savedInvoice);
        } catch (Exception e) {
            log.error("Error generating fee invoice: {}", e.getMessage());
            throw new RuntimeException("Failed to generate fee invoice: " + e.getMessage());
        }
    }

    @Override
    public StudentFeeDTO getStudentFeeById(Long id) {
        StudentFee studentFee = studentFeeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Student fee not found"));
        return convertToDTO(studentFee);
    }

    @Override
    public List<StudentFeeDTO> getStudentFeesByStudentId(Long studentId) {
        return studentFeeRepository.findByStudentId(studentId)
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<StudentFeeDTO> getOverdueInvoices() {
        return studentFeeRepository.findOverdueInvoices(LocalDate.now())
                .stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void applyLateFee(Long studentFeeId, Double lateFeeAmount) {
        StudentFee studentFee = studentFeeRepository.findById(studentFeeId)
                .orElseThrow(() -> new RuntimeException("Student fee not found"));

        studentFee.setLateFine(studentFee.getLateFine() + lateFeeAmount);
        studentFee.setTotalAmount(studentFee.getTotalAmount() + lateFeeAmount);

        if (studentFee.getDueDate().isBefore(LocalDate.now())) {
            studentFee.setStatus(FeeInvoiceStatus.OVERDUE);
        }

        studentFeeRepository.save(studentFee);
        log.info("Late fee applied to invoice {}: {}", studentFee.getInvoiceNumber(), lateFeeAmount);
    }

    private String generateInvoiceNumber() {
        String prefix = "INV";
        String timestamp = String.valueOf(System.currentTimeMillis());
        return prefix + timestamp.substring(timestamp.length() - 8);
    }

    private StudentFeeDTO convertToDTO(StudentFee studentFee) {
        Double paidAmount = feePaymentRepository.getTotalPaidAmountByStudentFeeId(studentFee.getId());
        Double remainingAmount = studentFee.getTotalAmount() - (paidAmount != null ? paidAmount : 0.0);

        return StudentFeeDTO.builder()
                .id(studentFee.getId())
                .studentId(studentFee.getStudent().getId())
                .studentName(studentFee.getStudent().getName())
                .feeStructureId(studentFee.getFeeStructure().getId())
                .feeStructureName(studentFee.getFeeStructure().getProgram() + " - Semester " + studentFee.getFeeStructure().getSemester())
                .totalAmount(studentFee.getTotalAmount())
                .paidAmount(paidAmount != null ? paidAmount : 0.0)
                .remainingAmount(remainingAmount)
                .dueDate(studentFee.getDueDate())
                .status(studentFee.getStatus().name())
                .build();
    }
}