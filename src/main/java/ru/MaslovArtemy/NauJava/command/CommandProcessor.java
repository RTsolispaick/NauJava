package ru.MaslovArtemy.NauJava.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.service.TransactionService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class CommandProcessor {
    private final TransactionService transactionService;
    private final TransactionTableGenerator transactionTableGenerator;

    @Value("${help.commands}")
    private String helpCommands;

    @Autowired
    public CommandProcessor(TransactionService transactionService, TransactionTableGenerator transactionTableGenerator) {
        this.transactionService = transactionService;
        this.transactionTableGenerator = transactionTableGenerator;
    }

    public void processCommand(String input) {
        String[] cmd = Arrays.stream(input.split(" "))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
        switch (cmd[0]) {
            case "help" -> System.out.println(helpCommands);
            case "add" -> {
                try {
                    Double amount = Double.parseDouble(cmd[1]);
                    String category = cmd[2].toLowerCase();
                    LocalDate transactionDate = LocalDate.parse(cmd[3]);
                    Transaction.Type type = Transaction.Type.fromString(cmd[4]);
                    String description = String.join(" ", Arrays.copyOfRange(cmd, 5, cmd.length));

                    transactionService.createTransaction(amount,
                            category,
                            transactionDate,
                            type,
                            description);
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    System.out.println("""
                            Некорректный ввод!
                            Ведите запись по формату: add xx.xx category YYYY-MM-DD type(+, -), description""");
                }
            }
            case "delete" -> {
                try {
                    transactionService.deleteById(Long.valueOf(cmd[1]));
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                    System.out.println("""
                            Некорректный ввод!
                            Для удаления записи: delete id
                            """);
                }
            }
            case "getAll" -> {
                Optional<List<Transaction>> transactionOptional = transactionService.getAll();

                printOptionalListTransaction(transactionOptional);
            }
            case "getById" -> {
                long id;
                try {
                    id = Long.parseLong(cmd[1]);
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                    System.out.println("""
                            Некорректный ввод!
                            Для поиска записи по id: getById id
                            """);
                    return;
                }

                Optional<Transaction> transactionOptional = transactionService.findById(id);

                transactionOptional.ifPresentOrElse(
                        transaction -> System.out.println(transactionTableGenerator
                                .getTableTransaction(List.of(transaction))),
                        () -> System.out.println("Транзакция с таким id не найдена!")
                );
            }
            case "findByCategory" -> {
                String category = cmd[1];

                Optional<List<Transaction>> transactionsByCategory = transactionService.findByFilter(
                        transaction -> transaction.getCategory().equals(category)
                );

                printOptionalListTransaction(transactionsByCategory);
            }
            case "findByDate" -> {
                LocalDate localDate;
                try {
                    localDate = LocalDate.parse(cmd[1]);
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                    System.out.println("""
                            Некорректный ввод!
                            Для поиска транзакций по дате: findByDate yyyy-MM-dd
                            """);
                    return;
                }

                Optional<List<Transaction>> transactionsByDate = transactionService.findByFilter(
                        transaction ->  transaction.getDate().equals(localDate)
                );

                printOptionalListTransaction(transactionsByDate);
            }
            case "findByMonth" -> {
                LocalDate localDate;
                try {
                    localDate = LocalDate.parse(cmd[1] + "-01");
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                    System.out.println("""
                            Некорректный ввод!
                            Для поиска транзакций по дате: findByDate yyyy-MM
                            """);
                    return;
                }

                Optional<List<Transaction>> transactionsByMonth = transactionService.findByFilter(
                        transaction -> transaction.getDate().getYear() == localDate.getYear()
                                && transaction.getDate().getMonth() == localDate.getMonth()
                );

                printOptionalListTransaction(transactionsByMonth);
            }
            case "findByYear" -> {
                LocalDate localDate;
                try {
                    localDate = LocalDate.parse(cmd[1] + "-01-01");
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                    System.out.println("""
                            Некорректный ввод!
                            Для поиска транзакций по дате: findByDate yyyy
                            """);
                    return;
                }

                Optional<List<Transaction>> transactionsByYear = transactionService.findByFilter(
                        transaction -> transaction.getDate().getYear() == localDate.getYear()
                );

                printOptionalListTransaction(transactionsByYear);
            }
            case "sumByCategory" -> {
                String category = cmd[1];

                Optional<List<Transaction>> transactionsByCategory = transactionService.findByFilter(
                        transaction -> transaction.getCategory().equals(category)
                );

                Double sum = getSumOptionalListTransaction(transactionsByCategory);

                System.out.println("Cумма по категории " + category + ": " + sum);
            }
            case "sumByDate" -> {
                LocalDate localDate;
                try {
                    localDate = LocalDate.parse(cmd[1]);
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                    System.out.println("""
                            Некорректный ввод!
                            Для поиска транзакций по дате: findByDate yyyy-MM-dd
                            """);
                    return;
                }

                Optional<List<Transaction>> optionalTransactions = transactionService.findByFilter(
                        transaction -> transaction.getDate().equals(localDate)
                );

                Double sum = getSumOptionalListTransaction(optionalTransactions);

                System.out.println("Сумма за день: " + sum);
            }
            case "sumByMonth" -> {
                LocalDate localDate;
                try {
                    localDate = LocalDate.parse(cmd[1] + "-01");
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                    System.out.println("""
                            Некорректный ввод!
                            Для поиска транзакций по дате: findByDate yyyy-MM
                            """);
                    return;
                }

                Optional<List<Transaction>> transactionsByMonth = transactionService.findByFilter(
                        transaction -> transaction.getDate().getYear() == localDate.getYear()
                                && transaction.getDate().getMonth() == localDate.getMonth()
                );

                Double sum = getSumOptionalListTransaction(transactionsByMonth);

                System.out.println("Сумма за месяц: " + sum);
            }
            case "sumByYear" -> {
                LocalDate localDate;
                try {
                    localDate = LocalDate.parse(cmd[1] + "-01-01");
                } catch (IllegalStateException e) {
                    System.out.println(e.getMessage());
                    System.out.println("""
                            Некорректный ввод!
                            Для поиска транзакций по дате: findByDate yyyy
                            """);
                    return;
                }

                Optional<List<Transaction>> transactionsByYear = transactionService.findByFilter(
                        transaction -> transaction.getDate().getYear() == localDate.getYear()
                );

                Double sum = getSumOptionalListTransaction(transactionsByYear);

                System.out.println("Сумма за год: " + sum);
            }
            default -> System.out.println("Неизвестная команда!");
        }
    }

    private Double getSumOptionalListTransaction(Optional<List<Transaction>> optionalTransactions) {
         return optionalTransactions.map(transactions ->
                transactions.stream()
                        .mapToDouble(transaction -> transaction.getType().getInteger() * transaction.getAmount())
                        .sum()
         ).orElse(0.0);
    }

    private void printOptionalListTransaction(Optional<List<Transaction>> transactions) {
        transactions.ifPresentOrElse(
                transaction -> System.out.println(transactionTableGenerator.getTableTransaction(transaction)),
                () -> System.out.println("Список транзакций пуст!")
        );
    }
}
