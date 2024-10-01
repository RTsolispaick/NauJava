package ru.MaslovArtemy.NauJava.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.service.TransactionService;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Обработчик команд для управления транзакциями.
 * Этот класс отвечает за обработку команд, связанных с транзакциями,
 * включая добавление, удаление, получение и фильтрацию транзакций.
 */
@Component
class TransactionCommandHandler {
    private final TransactionService transactionService;
    private final TransactionTableGenerator transactionTableGenerator;

    /**
     * Конструктор для создания обработчика команд транзакций.
     *
     * @param transactionService сервис для управления транзакциями
     * @param transactionTableGenerator генератор таблицы транзакций для отображения
     */
    @Autowired
    public TransactionCommandHandler(TransactionService transactionService, TransactionTableGenerator transactionTableGenerator) {
        this.transactionService = transactionService;
        this.transactionTableGenerator = transactionTableGenerator;
    }

    /**
     * Обрабатывает команду для добавления новой транзакции.
     *
     * @param cmd массив строк, содержащий данные для создания транзакции
     */
    public void handleAddCommand(String[] cmd) {
        try {
            Double amount = Double.parseDouble(cmd[1]);
            String category = cmd[2].toLowerCase();
            LocalDate transactionDate = LocalDate.parse(cmd[3]);
            Transaction.Type type = Transaction.Type.fromString(cmd[4]);
            String description = String.join(" ", Arrays.copyOfRange(cmd, 5, cmd.length));

            transactionService.createTransaction(amount, category, transactionDate, type, description);
            System.out.println("Транзакция успешно добавлена!");
        } catch (Exception e) {
            printErrorMessage("Некорректный ввод! Введите запись по формату: add xx.xx category YYYY-MM-DD type(+, -), description");
        }
    }

    /**
     * Обрабатывает команду для удаления транзакции по её идентификатору.
     *
     * @param cmd массив строк, содержащий идентификатор транзакции для удаления
     */
    public void handleDeleteCommand(String[] cmd) {
        try {
            long id = Long.parseLong(cmd[1]);
            transactionService.deleteById(id);
            System.out.println("Транзакция успешно удалена!");
        } catch (NumberFormatException e) {
            printErrorMessage("Некорректный ввод! Для удаления записи введите: delete id");
        } catch (IllegalStateException e) {
            printErrorMessage("Ошибка удаления транзакции: " + e.getMessage());
        }
    }

    /**
     * Обрабатывает команду для получения всех транзакций.
     */
    public void handleGetAllCommand() {
        Optional<List<Transaction>> transactionOptional = transactionService.getAll();
        printOptionalListTransaction(transactionOptional);
    }

    /**
     * Обрабатывает команду для получения транзакции по её идентификатору.
     *
     * @param cmd массив строк, содержащий идентификатор транзакции для поиска
     */
    public void handleGetByIdCommand(String[] cmd) {
        try {
            long id = Long.parseLong(cmd[1]);
            Optional<Transaction> transactionOptional = transactionService.findById(id);
            transactionOptional.ifPresentOrElse(
                    transaction -> System.out.println(transactionTableGenerator.getTableTransaction(List.of(transaction))),
                    () -> System.out.println("Транзакция с таким id не найдена!")
            );
        } catch (NumberFormatException e) {
            printErrorMessage("Некорректный ввод! Для поиска записи по id: getById id");
        }
    }

    /**
     * Обрабатывает команду для поиска транзакций по категории.
     *
     * @param cmd массив строк, содержащий категорию для фильтрации транзакций
     */
    public void handleFindByCategoryCommand(String[] cmd) {
        String category = cmd[1].toLowerCase();
        Optional<List<Transaction>> transactionsByCategory = transactionService.findByFilter(
                transaction -> transaction.category().equals(category)
        );
        printOptionalListTransaction(transactionsByCategory);
    }

    /**
     * Обрабатывает команду для поиска транзакций по дате.
     *
     * @param cmd массив строк, содержащий дату для фильтрации транзакций
     */
    public void handleFindByDateCommand(String[] cmd) {
        LocalDate localDate = parseDate(cmd[1]);
        if (localDate == null) {
            System.out.println("Некорректный ввод! Формат даты: yyyy-MM-dd");
            return;
        }

        Optional<List<Transaction>> transactionsByDate = transactionService.findByFilter(
                transaction -> transaction.date().equals(localDate)
        );
        printOptionalListTransaction(transactionsByDate);
    }

    /**
     * Обрабатывает команду для поиска транзакций по месяцу.
     *
     * @param cmd массив строк, содержащий месяц для фильтрации транзакций
     */
    public void handleFindByMonthCommand(String[] cmd) {
        LocalDate localDate = parseDate(cmd[1] + "-01");
        if (localDate == null) {
            System.out.println("Некорректный ввод! Формат даты: yyyy-MM");
            return;
        }

        Optional<List<Transaction>> transactionsByMonth = transactionService.findByFilter(
                transaction -> transaction.date().getYear() == localDate.getYear()
                        && transaction.date().getMonth() == localDate.getMonth()
        );
        printOptionalListTransaction(transactionsByMonth);
    }

    /**
     * Обрабатывает команду для поиска транзакций по году.
     *
     * @param cmd массив строк, содержащий год для фильтрации транзакций
     */
    public void handleFindByYearCommand(String[] cmd) {
        LocalDate localDate = parseDate(cmd[1] + "-01-01");
        if (localDate == null) {
            System.out.println("Некорректный ввод! Формат даты: yyyy");
            return;
        }

        Optional<List<Transaction>> transactionsByYear = transactionService.findByFilter(
                transaction -> transaction.date().getYear() == localDate.getYear()
        );
        printOptionalListTransaction(transactionsByYear);
    }

    /**
     * Обрабатывает команду для вычисления суммы транзакций по категории.
     *
     * @param cmd массив строк, содержащий категорию для суммирования транзакций
     */
    public void handleSumByCategoryCommand(String[] cmd) {
        String category = cmd[1].toLowerCase();
        Optional<List<Transaction>> transactionsByCategory = transactionService.findByFilter(
                transaction -> transaction.category().equals(category)
        );

        Double sum = getSumOptionalListTransaction(transactionsByCategory);
        System.out.println("Сумма по категории " + category + ": " + sum);
    }

    /**
     * Обрабатывает команду для вычисления суммы транзакций за день.
     *
     * @param cmd массив строк, содержащий дату для суммирования транзакций
     */
    public void handleSumByDateCommand(String[] cmd) {
        LocalDate localDate = parseDate(cmd[1]);
        if (localDate == null) {
            System.out.println("Некорректный ввод! Формат даты: yyyy-MM-dd");
            return;
        }

        Optional<List<Transaction>> optionalTransactions = transactionService.findByFilter(
                transaction -> transaction.date().equals(localDate)
        );

        Double sum = getSumOptionalListTransaction(optionalTransactions);
        System.out.println("Сумма за день: " + sum);
    }

    /**
     * Обрабатывает команду для вычисления суммы транзакций за месяц.
     *
     * @param cmd массив строк, содержащий месяц для суммирования транзакций
     */
    public void handleSumByMonthCommand(String[] cmd) {
        LocalDate localDate = parseDate(cmd[1] + "-01");
        if (localDate == null) {
            System.out.println("Некорректный ввод! Формат даты: yyyy-MM");
            return;
        }

        Optional<List<Transaction>> transactionsByMonth = transactionService.findByFilter(
                transaction -> transaction.date().getYear() == localDate.getYear()
                        && transaction.date().getMonth() == localDate.getMonth()
        );

        Double sum = getSumOptionalListTransaction(transactionsByMonth);
        System.out.println("Сумма за месяц: " + sum);
    }

    /**
     * Обрабатывает команду для вычисления суммы транзакций за год.
     *
     * @param cmd массив строк, содержащий год для суммирования транзакций
     */
    public void handleSumByYearCommand(String[] cmd) {
        LocalDate localDate = parseDate(cmd[1] + "-01-01");
        if (localDate == null) {
            System.out.println("Некорректный ввод! Формат даты: yyyy");
            return;
        }

        Optional<List<Transaction>> transactionsByYear = transactionService.findByFilter(
                transaction -> transaction.date().getYear() == localDate.getYear()
        );

        Double sum = getSumOptionalListTransaction(transactionsByYear);
        System.out.println("Сумма за год: " + sum);
    }

    /**
     * Парсит строку даты в объект LocalDate.
     *
     * @param date строка даты в формате yyyy-MM-dd
     * @return объект LocalDate, если парсинг успешен, иначе null
     */
    private LocalDate parseDate(String date) {
        try {
            return LocalDate.parse(date);
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /**
     * Вычисляет сумму транзакций из списка, если таковой существует.
     *
     * @param optionalTransactions {@link Optional} список транзакций
     * @return сумма транзакций
     */
    private Double getSumOptionalListTransaction(Optional<List<Transaction>> optionalTransactions) {
        return optionalTransactions.map(transactions ->
                transactions.stream()
                        .mapToDouble(transaction -> transaction.type().getInteger() * transaction.amount())
                        .sum()
        ).orElse(0.0);
    }

    /**
     * Выводит список транзакций или сообщение о пустом списке.
     *
     * @param transactions {@link Optional} список транзакций
     */
    private void printOptionalListTransaction(Optional<List<Transaction>> transactions) {
        transactions.ifPresentOrElse(
                transaction -> System.out.println(transactionTableGenerator.getTableTransaction(transaction)),
                () -> System.out.println("Список транзакций пуст!")
        );
    }

    /**
     * Выводит сообщение об ошибке.
     *
     * @param message сообщение для вывода
     */
    private void printErrorMessage(String message) {
        System.out.println(message);
    }
}
