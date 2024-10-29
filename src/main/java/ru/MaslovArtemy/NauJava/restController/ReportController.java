package ru.MaslovArtemy.NauJava.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.MaslovArtemy.NauJava.service.ReportService;

import java.util.Arrays;

@RestController
@RequestMapping("/custom/reports")
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

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Exception exceptionCategoryNotFound(Exception e) {
        Exception exception = new Exception(e.getMessage());
        exception.setStackTrace(Arrays.stream(e.getStackTrace()).limit(5).toArray(StackTraceElement[]::new));
        return exception;
    }
}
