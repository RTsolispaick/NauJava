package ru.MaslovArtemy.NauJava.service;

import ru.MaslovArtemy.NauJava.model.Category;

import java.util.Optional;

public interface CategoryService {
    Category createCategory(String name, String description);

    Optional<Category> getCategoryById(Long id);

    Iterable<Category> getAllCategories();

    Category updateCategory(Long id, String name, String description);

    void deleteCategory(Long id);
}
