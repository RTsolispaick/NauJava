package ru.MaslovArtemy.NauJava.ui.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.MaslovArtemy.NauJava.model.context.CurrentUser;
import ru.MaslovArtemy.NauJava.output.Printer;

@Component
public class LogoutCommand implements Command {
    private final CurrentUser currentUser;
    private final Printer printer;

    @Autowired
    public LogoutCommand(CurrentUser currentUser, Printer printer) {
        this.currentUser = currentUser;
        this.printer = printer;
    }

    @Override
    public String getName() {
        return "logout";
    }

    @Override
    public void execute(String[] args) {
        if (currentUser == null) {
            printer.print("Вы и так не находитесь в аккаунте!\n");
            return;
        }

        currentUser.setUser(null);

        printer.print("Вы вышли из аккаунта!\n");
    }
}
