package ru.MaslovArtemy.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.MaslovArtemy.NauJava.model.Report;
import ru.MaslovArtemy.NauJava.model.ReportStatus;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.repository.ReportRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.StreamSupport;

@Service
public class ReportServiceImpl implements ReportService {
    private final ReportRepository reportRepository;
    private final TransactionService transactionService;
    private final UserService userService;

    @Autowired
    public ReportServiceImpl(ReportRepository reportRepository, TransactionService transactionService, UserService userService) {
        this.reportRepository = reportRepository;
        this.transactionService = transactionService;
        this.userService = userService;
    }

    @Override
    public String getReportContent(Long reportId) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Отчет с id " + reportId + " не найден"));

        return switch (report.getStatus()) {
            case ReportStatus.COMPLETED -> report.getContent();
            case ReportStatus.CREATED -> "Отчёт не сформирован!";
            case ReportStatus.ERROR -> "Ошибка при формировании отчёта!";
        };
    }

    @Override
    public Long createReport() {
        Report report = new Report();
        report.setStatus(ReportStatus.CREATED);
        report = reportRepository.save(report);
        return report.getId();
    }

    @Override
    public CompletableFuture<Void> generateReport(Long reportId) {
        return CompletableFuture.runAsync(() -> {
            long startTime = System.currentTimeMillis();

            CompletableFuture<QueryResult<Long>> userCountFuture = CompletableFuture.supplyAsync(() -> {
                long userStartTime = System.currentTimeMillis();
                Long userCount = StreamSupport.stream(userService.getAllUsers().spliterator(), false).count();
                long userElapsedTime = System.currentTimeMillis() - userStartTime;

                return new QueryResult<>(userCount, userElapsedTime);
            });

            CompletableFuture<QueryResult<List<Transaction>>> transactionListFuture = CompletableFuture.supplyAsync(() -> {
                long transactionStartTime = System.currentTimeMillis();
                List<Transaction> transactions = (List<Transaction>) transactionService.getAllTransactions();
                long transactionElapsedTime = System.currentTimeMillis() - transactionStartTime;

                return new QueryResult<>(transactions, transactionElapsedTime);
            });

            try {
                QueryResult<Long> userCount = userCountFuture.join();
                QueryResult<List<Transaction>> transactions = transactionListFuture.join();
                long totalElapsedTime = System.currentTimeMillis() - startTime;

                String reportContent = generateHtmlReport(userCount, transactions, totalElapsedTime);
                updateReportStatus(reportId, ReportStatus.COMPLETED, reportContent);
            } catch (Exception e) {
                updateReportStatus(reportId, ReportStatus.ERROR, e.getMessage());
            }
        });
    }

    private void updateReportStatus(Long reportId, ReportStatus status, String content) {
        Report report = reportRepository.findById(reportId)
                .orElseThrow(() -> new IllegalArgumentException("Отчет с id " + reportId + " не найден"));

        report.setStatus(status);
        report.setContent(content);
        reportRepository.save(report);
    }

    private String generateHtmlReport(QueryResult<Long> userCount, QueryResult<List<Transaction>> transactions, long totalElapsedTime) {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><body>");
        htmlBuilder.append("<h1>Отчет статистики приложения</h1>");
        htmlBuilder.append("<p>Количество зарегистрированных пользователей: ").append(userCount.result()).append("</p>");
        htmlBuilder.append("<h2>Список транзакций</h2>");
        htmlBuilder.append("<table border='1'>");
        htmlBuilder.append("<tr><th>ID</th><th>Сумма</th><th>Дата</th><th>Описание</th></tr>");

        for (Transaction transaction : transactions.result()) {
            htmlBuilder.append("<tr><td>").append(transaction.getId()).append("</td>")
                    .append("<td>").append(transaction.getAmount()).append("</td>")
                    .append("<td>").append(transaction.getDate()).append("</td>")
                    .append("<td>").append(transaction.getDescription()).append("</td></tr>");
        }
        htmlBuilder.append("</table>");

        htmlBuilder.append("<p>Время подсчёта пользователей: ").append(userCount.executionTime()).append(" ms</p>");
        htmlBuilder.append("<p>Время получения всех транзакций: ").append(transactions.executionTime()).append(" ms</p>");
        htmlBuilder.append("<p>Общее время формирования отчета: ").append(totalElapsedTime).append(" ms</p>");
        htmlBuilder.append("</body></html>");
        return htmlBuilder.toString();
    }

    private record QueryResult<T>(T result, long executionTime) {}
}
