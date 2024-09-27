package ru.MaslovArtemy.NauJava.command;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.MaslovArtemy.NauJava.service.UserService;

@Component
public class CommandProcessor {
    private final UserService userService;

    @Autowired
    public CommandProcessor(UserService userService) {
        this.userService = userService;
    }

    public void processCommand(String input) {
        String[] cmd = input.split(" ");
        switch (cmd[0]) {
            case "create" -> {
                userService.createUser(Long.valueOf(cmd[1]), cmd[2]);
                System.out.println("Пользователь успешно добавлен...");
            }
// .... здесь логика Обработки других команды
            default -> System.out.println("Введена неизвестная команда...");
        }
    }
}
