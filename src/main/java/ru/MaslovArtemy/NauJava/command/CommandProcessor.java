package ru.MaslovArtemy.NauJava.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class CommandProcessor {
    private final TransactionCommandHandler transactionCommandHandler;

    @Value("${help.commands}")
    private String helpCommands;

    @Autowired
    public CommandProcessor(TransactionCommandHandler transactionCommandHandler) {
        this.transactionCommandHandler = transactionCommandHandler;
    }

    public void processCommand(String input) {
        String[] cmd = Arrays.stream(input.split(" "))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);
        switch (cmd[0]) {
            case "help" -> System.out.println(helpCommands);
            case "add" -> transactionCommandHandler.handleAddCommand(cmd);
            case "delete" -> transactionCommandHandler.handleDeleteCommand(cmd);
            case "getAll" -> transactionCommandHandler.handleGetAllCommand();
            case "getById" -> transactionCommandHandler.handleGetByIdCommand(cmd);
            case "findByCategory" -> transactionCommandHandler.handleFindByCategoryCommand(cmd);
            case "findByDate" -> transactionCommandHandler.handleFindByDateCommand(cmd);
            case "findByMonth" -> transactionCommandHandler.handleFindByMonthCommand(cmd);
            case "findByYear" -> transactionCommandHandler.handleFindByYearCommand(cmd);
            case "sumByCategory" -> transactionCommandHandler.handleSumByCategoryCommand(cmd);
            case "sumByDate" -> transactionCommandHandler.handleSumByDateCommand(cmd);
            case "sumByMonth" -> transactionCommandHandler.handleSumByMonthCommand(cmd);
            case "sumByYear" -> transactionCommandHandler.handleSumByYearCommand(cmd);
            default -> System.out.println("Неизвестная команда!");
        }
    }
}
