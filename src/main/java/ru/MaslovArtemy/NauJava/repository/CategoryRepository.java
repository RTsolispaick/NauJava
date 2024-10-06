package ru.MaslovArtemy.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.MaslovArtemy.NauJava.model.Category;

public interface CategoryRepository extends CrudRepository<Category, Long> {
}
