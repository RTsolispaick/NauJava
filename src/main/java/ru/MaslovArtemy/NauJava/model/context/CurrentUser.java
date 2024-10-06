package ru.MaslovArtemy.NauJava.model.context;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import ru.MaslovArtemy.NauJava.model.User;

@Setter
@Getter
@Component
public class CurrentUser {
    private User user;
}

