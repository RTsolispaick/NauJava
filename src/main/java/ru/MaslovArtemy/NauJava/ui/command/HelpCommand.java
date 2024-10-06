package ru.MaslovArtemy.NauJava.ui.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import ru.MaslovArtemy.NauJava.output.Printer;

@Component
public class HelpCommand implements Command {
    @Value("${help.commands}")
    private String helpCommands;
    private final Printer printer;

    @Autowired
    public HelpCommand(Printer printer) {
        this.printer = printer;
    }

    @Override
    public String getName() {
        return "help";
    }

    @Override
    public void execute(String[] args) {
        printer.print(helpCommands);
    }
}
