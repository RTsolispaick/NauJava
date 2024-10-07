package ru.MaslovArtemy.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.MaslovArtemy.NauJava.model.Category;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.model.User;

import java.util.List;
import java.util.Optional;

public interface CategoryRepository extends CrudRepository<Category, Long> {
    Optional<Category> findByName(String login);
}
