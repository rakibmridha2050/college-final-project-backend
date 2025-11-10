package com.rakib.collegeERPsystem.entity.payment2;


import com.rakib.collegeERPsystem.service.ReportService;
import lombok.RequiredArgsConstructor;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.HtmlExporter;
import net.sf.jasperreports.engine.export.JRCsvExporter;
import net.sf.jasperreports.engine.export.ooxml.JRDocxExporter;
import net.sf.jasperreports.engine.export.ooxml.JRXlsxExporter;
import net.sf.jasperreports.export.SimpleExporterInput;
import net.sf.jasperreports.export.SimpleHtmlExporterOutput;
import net.sf.jasperreports.export.SimpleOutputStreamExporterOutput;
import net.sf.jasperreports.export.SimpleWriterExporterOutput;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RequiredArgsConstructor
@RequestMapping("/api/reports")
@RestController
public class ReportController {
    private final ReportService reportService;
    @GetMapping("/payments/{format}")
    public ResponseEntity<byte[]> generateReport(@PathVariable String format) throws IOException, JRException {

        // Call the service method to get JasperPrint
        JasperPrint jasperPrint = reportService.getJasperPrint();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String fileName;
        MediaType mediaType;

        switch (format.toLowerCase()) {
            case "pdf":
                JasperExportManager.exportReportToPdfStream(jasperPrint, outputStream);
                fileName = "payments_report.pdf";
                mediaType = MediaType.APPLICATION_PDF;
                break;

            case "html":
                HtmlExporter htmlExporter = new HtmlExporter();
                htmlExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                ByteArrayOutputStream htmlOutput = new ByteArrayOutputStream();
                htmlExporter.setExporterOutput(new SimpleHtmlExporterOutput(htmlOutput));
                htmlExporter.exportReport();

                fileName = "payments_report.html";
                mediaType = MediaType.TEXT_HTML;
                outputStream = htmlOutput; // assign to main output stream
                break;


            case "xlsx":
                JRXlsxExporter xlsxExporter = new JRXlsxExporter();
                xlsxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                xlsxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                xlsxExporter.exportReport();
                fileName = "payments_report.xlsx";
                mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
                break;

            case "csv":
                JRCsvExporter csvExporter = new JRCsvExporter();
                csvExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                csvExporter.setExporterOutput(new SimpleWriterExporterOutput(outputStream));
                csvExporter.exportReport();
                fileName = "payments_report.csv";
                mediaType = MediaType.TEXT_PLAIN;
                break;

            case "docx":
                JRDocxExporter docxExporter = new JRDocxExporter();
                docxExporter.setExporterInput(new SimpleExporterInput(jasperPrint));
                docxExporter.setExporterOutput(new SimpleOutputStreamExporterOutput(outputStream));
                docxExporter.exportReport();
                fileName = "payments_report.docx";
                mediaType = MediaType.parseMediaType("application/vnd.openxmlformats-officedocument.wordprocessingml.document");
                break;

            default:
                return ResponseEntity.badRequest().body(("⚠️ Invalid format: " + format).getBytes());
        }

        // Return file as download
        return ResponseEntity.ok()
                .contentType(mediaType)
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + fileName)
                .body(outputStream.toByteArray());
    }
}
