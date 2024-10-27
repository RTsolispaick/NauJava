package ru.MaslovArtemy.NauJava.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.MaslovArtemy.NauJava.service.ReportService;

@RestController
@RequestMapping("/api/reports")
public class ReportController {
    private final ReportService reportService;

    @Autowired
    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @PostMapping
    public ResponseEntity<Long> createReport() {
        Long reportId = reportService.createReport();
        reportService.generateReport(reportId);
        return ResponseEntity.ok(reportId);
    }

    @GetMapping("/{id}")
    public ResponseEntity<String> getReportContentById(@PathVariable Long id) {
        return ResponseEntity.ok(reportService.getReportContent(id));
    }
}
