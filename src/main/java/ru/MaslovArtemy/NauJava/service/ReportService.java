package ru.MaslovArtemy.NauJava.service;

import java.util.concurrent.CompletableFuture;

public interface ReportService {
    String getReportContent(Long reportId);
    Long createReport();
    CompletableFuture<Void> generateReport(Long reportId);
}
