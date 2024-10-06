package ru.MaslovArtemy.NauJava.ui.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.MaslovArtemy.NauJava.model.User;
import ru.MaslovArtemy.NauJava.model.context.CurrentUser;
import ru.MaslovArtemy.NauJava.output.Printer;
import ru.MaslovArtemy.NauJava.service.UserService;

import java.util.Optional;

@Component
public class LoginCommand implements Command {
    private final UserService userService;
    private final CurrentUser currentUser;
    private final Printer printer;

    @Autowired
    public LoginCommand(UserService userService, CurrentUser currentUser, Printer printer) {
        this.userService = userService;
        this.currentUser = currentUser;
        this.printer = printer;
    }


    @Override
    public String getName() {
        return "login";
    }

    @Override
    public void execute(String[] args) {
        if (args.length < 2) {
            printer.print("Используй: login <username> <password>\n");
            return;
        } else if (currentUser.getUser() != null) {
            printer.print("Для авториазции сначала выйдите с аккаунта!\n");
            return;
        }

        String login = args[0];
        String password = args[1];

        Optional<User> user = userService.getUserByName(login);

        user.ifPresentOrElse(user1 -> {
            if (password.equals(user1.getPassword())) {
                currentUser.setUser(user1);
                printer.print("Вход в аккаунт " + user1.getName() + " выполнен!\n");
            } else {
                printer.print("Неверный логин или пароль!\n");
            }
        }, () -> printer.print("Такой пользователь не найден!\n"));
    }
}
