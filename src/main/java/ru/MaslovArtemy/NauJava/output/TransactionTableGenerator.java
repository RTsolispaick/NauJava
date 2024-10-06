package ru.MaslovArtemy.NauJava.output;

import org.springframework.stereotype.Component;
import ru.MaslovArtemy.NauJava.model.Transaction;

import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Генератор таблицы для отображения транзакций.
 * Этот класс отвечает за форматирование списка транзакций в виде таблицы.
 */
@Component
class TransactionTableGenerator {

    /**
     * Генерирует строковое представление таблицы транзакций.
     *
     * @param transactions список транзакций для отображения
     * @return строка, содержащая отформатированную таблицу транзакций
     */
    public String getTableTransaction(List<Transaction> transactions) {
        StringBuilder tableBuilder = new StringBuilder();
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd"); // Форматирование даты

        tableBuilder.append(String.format("%-10s %-10s %-15s %-10s %-10s %-30s%n",
                "ID", "Amount", "Category", "Date", "Type", "Description"));
        tableBuilder.append("---------------------------------------------------------------------------------")
                .append(System.lineSeparator());

        for (Transaction transaction : transactions) {
            Long key = transaction.getId();  // Получаем ID транзакции

            tableBuilder.append(String.format("%-10d %-10.2f %-15s %-10s %-10s %-30s%n",
                    key,
                    transaction.getAmount(),
                    transaction.getCategory(),
                    transaction.getDate(), // Форматируем дату
                    transaction.getType(),
                    transaction.getDescription()));
        }

        return tableBuilder.toString();
    }
}
