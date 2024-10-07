package ru.MaslovArtemy.NauJava.ui.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.model.context.CurrentUser;
import ru.MaslovArtemy.NauJava.output.Printer;
import ru.MaslovArtemy.NauJava.service.TransactionService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

@Component
public class AddTransactionCommand implements Command {
    private final TransactionService transactionService;
    private final CurrentUser currentUser;
    private final Printer printer;

    @Autowired
    public AddTransactionCommand(TransactionService transactionService, CurrentUser currentUser, Printer printer) {
        this.transactionService = transactionService;
        this.printer = printer;
        this.currentUser = currentUser;
    }


    @Override
    public String getName() {
        return "addTransaction";
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 6) {
            printer.print("Недостаточно аргументов.\n" +
                    "Использование: addTransaction <сумма> <дата> <описание> <тип>('+', '-') <budgetId> <category>\n");
            return;
        } else if (currentUser.getUser() == null) {
            printer.print("Перед добавлением транзакции войдите в аккаунт!\n");
        }

        try {
            Float amount = Float.parseFloat(args[0]);
            Date date = new SimpleDateFormat("yyyy-MM-dd").parse(args[1]);
            String description = args[2];
            String type = args[3];

            if (!type.equals("+") && !type.equals("-")) {
                printer.print("Тип должен быть либо '+', либо '-'\n");
                return;
            }

            String budget = args[4];
            String category = args[5].toLowerCase();

            Transaction transaction = transactionService.createTransaction(amount, date, description, type, currentUser.getUser(), budget, category);

            printer.print("Транзакция успешно создана: " + transaction + "\n");
        } catch (NumberFormatException e) {
            printer.print("Неверный формат числа в аргументах.\n");
        } catch (ParseException e) {
            printer.print("Неверный формат даты. Используйте формат yyyy-MM-dd.\n");
        } catch (IllegalArgumentException e) {
            printer.print("Ошибка: " + e.getMessage() + "\n");
        } catch (Exception e) {
            printer.print("Произошла непредвиденная ошибка: " + e.getMessage() + "\n");
        }
    }
}
