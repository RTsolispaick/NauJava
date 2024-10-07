package ru.MaslovArtemy.NauJava.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import ru.MaslovArtemy.NauJava.output.Printer;

import java.util.Scanner;

@Configuration
public class CommandLineConfig {
    private final CommandProcessor commandProcessor;
    private final Printer printer;

    @Value("${greeting.message}")
    private String greetingMessage;

    @Autowired
    public CommandLineConfig(CommandProcessor commandProcessor, Printer printer) {
        this.commandProcessor = commandProcessor;
        this.printer = printer;
    }

    @Bean
    public CommandLineRunner commandScanner() {
        return args ->
        {
            try (Scanner scanner = new Scanner(System.in)) {
                printer.print(greetingMessage + "\n");
                printer.print("\n");
                printer.print("Введите команду. 'exit' для выхода.\n");
                while (true) {
                    printer.print("> ");
                    String input = scanner.nextLine();

                    if ("exit".equalsIgnoreCase(input.trim())) {
                        printer.print("Выход из программы...\n");
                        break;
                    }

                    commandProcessor.processCommand(input);
                }
            }
        };
    }
}
