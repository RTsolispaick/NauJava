package ru.MaslovArtemy.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.MaslovArtemy.NauJava.model.Report;
import ru.MaslovArtemy.NauJava.model.ReportStatus;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.repository.ReportRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.AtomicReference;
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

            AtomicLong userCount = new AtomicLong();
            AtomicLong userElapsedTime = new AtomicLong();
            AtomicReference<List<Transaction>> transactions = new AtomicReference<>();
            AtomicLong transactionElapsedTime = new AtomicLong();

            Thread userCountThread = new Thread(() -> {
                long userStartTime = System.currentTimeMillis();
                userCount.set(StreamSupport.stream(userService.getAllUsers().spliterator(), false).count());
                userElapsedTime.set(System.currentTimeMillis() - userStartTime);
            });


            Thread transactionListThread = new Thread(() -> {
                long transactionStartTime = System.currentTimeMillis();
                transactions.set((List<Transaction>) transactionService.getAllTransactions());
                transactionElapsedTime.set(System.currentTimeMillis() - transactionStartTime);
            });

            userCountThread.start();
            transactionListThread.start();

            try {
                userCountThread.join();
                transactionListThread.join();

                long totalElapsedTime = System.currentTimeMillis() - startTime;

                String reportContent = generateHtmlReport(
                        userCount.get(), userElapsedTime.get(),
                        transactions.get(), transactionElapsedTime.get(),
                        totalElapsedTime);
                updateReportStatus(reportId, ReportStatus.COMPLETED, reportContent);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Восстанавливаем статус прерывания
                updateReportStatus(reportId, ReportStatus.ERROR, e.getMessage());
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

    private String generateHtmlReport(Long userCount, Long userQueryTime,
                                      List<Transaction> transactions, Long transactionsQueryTime,
                                      Long totalElapsedTime) {
        StringBuilder htmlBuilder = new StringBuilder();
        htmlBuilder.append("<html><body>");
        htmlBuilder.append("<h1>Отчет статистики приложения</h1>");
        htmlBuilder.append("<p>Количество зарегистрированных пользователей: ").append(userCount).append("</p>");
        htmlBuilder.append("<h2>Список транзакций</h2>");
        htmlBuilder.append("<table border='1'>");
        htmlBuilder.append("<tr><th>ID</th><th>Сумма</th><th>Дата</th><th>Описание</th></tr>");

        for (Transaction transaction : transactions) {
            htmlBuilder.append("<tr><td>").append(transaction.getId()).append("</td>")
                    .append("<td>").append(transaction.getAmount()).append("</td>")
                    .append("<td>").append(transaction.getDate()).append("</td>")
                    .append("<td>").append(transaction.getDescription()).append("</td></tr>");
        }
        htmlBuilder.append("</table>");

        htmlBuilder.append("<p>Время подсчёта пользователей: ").append(userQueryTime).append(" ms</p>");
        htmlBuilder.append("<p>Время получения всех транзакций: ").append(transactionsQueryTime).append(" ms</p>");
        htmlBuilder.append("<p>Общее время формирования отчета: ").append(totalElapsedTime).append(" ms</p>");
        htmlBuilder.append("</body></html>");
        return htmlBuilder.toString();
    }
}
