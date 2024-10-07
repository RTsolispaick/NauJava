package ru.MaslovArtemy.NauJava.ui.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.MaslovArtemy.NauJava.model.User;
import ru.MaslovArtemy.NauJava.model.context.CurrentUser;
import ru.MaslovArtemy.NauJava.output.Printer;
import ru.MaslovArtemy.NauJava.service.UserService;

import java.util.Date;
import java.util.Optional;

@Component
public class RegistrationCommand implements Command {
    private final UserService userService;
    private final CurrentUser currentUser;
    private final Printer printer;

    @Autowired
    public RegistrationCommand(UserService userService, CurrentUser currentUser, Printer printer) {
        this.userService = userService;
        this.currentUser = currentUser;
        this.printer = printer;
    }

    @Override
    public String getName() {
        return "reg";
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            printer.print("Используй: reg <username> <password>\n");
            return;
        } else if (currentUser.getUser() != null) {
            printer.print("Для регистрации сначала выйдите с аккаунта!\n");
            return;
        }

        String login = args[0];
        String password = args[1];

        Optional<User> user = userService.getUserByName(login);

        user.ifPresentOrElse(user1 -> printer.print("Данное имя занято! Попоробуйте другое...\n"),
                () -> {
                    userService.createUser(login, password, new Date());
                    printer.print("Пользователь успешно создан!\n");
                });
    }
}
