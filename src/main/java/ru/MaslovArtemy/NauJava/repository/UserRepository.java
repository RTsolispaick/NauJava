package ru.MaslovArtemy.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.MaslovArtemy.NauJava.model.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByName(String login);
}
