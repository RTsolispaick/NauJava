package ru.MaslovArtemy.NauJava.service;

import ru.MaslovArtemy.NauJava.model.User;

import java.util.Date;
import java.util.Optional;

public interface UserService {
    User createUser(String name, String password, Date registrationDate);

    Optional<User> getUserById(Long id);

    Optional<User> getUserByName(String name);

    Iterable<User> getAllUsers();

    User updateUser(Long id, String name, String password);

    void deleteUser(Long id);
}
