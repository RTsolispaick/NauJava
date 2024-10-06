package ru.MaslovArtemy.NauJava.ui;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.MaslovArtemy.NauJava.output.Printer;
import ru.MaslovArtemy.NauJava.ui.command.Command;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommandProcessor {

    private final Map<String, Command> commands = new HashMap<>();
    private final Printer printer;

    @Autowired
    public CommandProcessor(List<Command> commandList, Printer printer) {
        for (Command command : commandList) {
            commands.put(command.getName(), command);
        }
        this.printer = printer;
    }

    public void processCommand(String input) {
        String[] cmd = Arrays.stream(input.split(" "))
                .filter(s -> !s.isEmpty())
                .toArray(String[]::new);

        try {
            String commandName = cmd[0];
            String[] args = Arrays.copyOfRange(cmd, 1, cmd.length);

            Command command = commands.get(commandName);
            if (command != null) {
                command.execute(args);
            } else {
                printer.print("Неизвестная команда: " + commandName + "\n");
            }
        } catch (ArrayIndexOutOfBoundsException e) {
            printer.print("""
                    Введите команду!
                    Для ознакомления с доступными командами используйте help
                    """);
        }
    }
}
