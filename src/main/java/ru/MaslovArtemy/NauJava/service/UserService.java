package ru.MaslovArtemy.NauJava.service;

import ru.MaslovArtemy.NauJava.model.User;

import java.util.Optional;

public interface UserService {
    User createUser(User user);

    Optional<User> getUserById(Long id);

    Optional<User> getUserByName(String name);

    Iterable<User> getAllUsers();

    User updateUser(User user);

    void deleteUser(User user);
}
