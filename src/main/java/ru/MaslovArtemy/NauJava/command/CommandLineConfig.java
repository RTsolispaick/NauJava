package ru.MaslovArtemy.NauJava.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Scanner;

@Configuration
public class CommandLineConfig {
    private final CommandProcessor commandProcessor;

    @Autowired
    public CommandLineConfig(CommandProcessor commandProcessor) {
        this.commandProcessor = commandProcessor;
    }

    @Bean
    public CommandLineRunner commandScanner() {
        return args ->
        {
            try (Scanner scanner = new Scanner(System.in)) {
                System.out.println("Введите команду. 'exit' для выхода.");
                while (true) {
                    System.out.print("> ");
                    String input = scanner.nextLine();

                    if ("exit".equalsIgnoreCase(input.trim())) {
                        System.out.println("Выход из программы...");
                        break;
                    }

                    commandProcessor.processCommand(input);
                }
            }
        };
    }
}
