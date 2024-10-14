package ru.MaslovArtemy.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.MaslovArtemy.NauJava.model.User;

import java.util.Optional;

@RepositoryRestResource(path = "user")
public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findByName(String login);
}
