package ru.MaslovArtemy.NauJava.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.MaslovArtemy.NauJava.model.User;

import java.util.List;

@Component
public class UserRepository implements CrudRepository<User, Long> {
    private final List<User> userContainer;

    @Autowired
    public UserRepository(List<User> userContainer) {
        this.userContainer = userContainer;
    }

    @Override
    public void create(User user) {
        // здесь логика добавления сущности в userContainer
    }

    @Override
    public User read(Long id) {
        // здесь логика получения сущности по id из userContainer
        return null;
    }

    @Override
    public void update(User user) {
        // здесь логика обновления сущности в userContainer
    }

    @Override
    public void delete(Long id) {
        // здесь логика удаления сущности из userContainer
    }
}
