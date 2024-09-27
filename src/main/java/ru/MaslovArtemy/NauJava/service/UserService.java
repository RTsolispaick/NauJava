package ru.MaslovArtemy.NauJava.service;

import ru.MaslovArtemy.NauJava.model.User;

public interface UserService {
    void createUser(Long id, String login);

    User findById(Long id);

    void deleteById(Long id);

    void updateLogin(Long id, String newLogin);
}
