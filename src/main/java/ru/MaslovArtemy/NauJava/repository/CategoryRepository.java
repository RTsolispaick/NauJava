package ru.MaslovArtemy.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.MaslovArtemy.NauJava.model.Category;

import java.util.Optional;

@RepositoryRestResource(path = "category")
public interface CategoryRepository extends CrudRepository<Category, Long> {
    Optional<Category> findByName(String name);
}
