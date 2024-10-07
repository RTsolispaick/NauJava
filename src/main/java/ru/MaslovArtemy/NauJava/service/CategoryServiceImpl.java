package ru.MaslovArtemy.NauJava.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import org.springframework.transaction.support.DefaultTransactionDefinition;
import ru.MaslovArtemy.NauJava.model.Category;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.repository.CategoryRepository;
import ru.MaslovArtemy.NauJava.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;

@Service
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;
    private final TransactionRepository transactionRepository;
    private final PlatformTransactionManager transactionManager;

    @Autowired
    public CategoryServiceImpl(CategoryRepository categoryRepository, TransactionRepository transactionRepository,
                               PlatformTransactionManager transactionManager) {
        this.categoryRepository = categoryRepository;
        this.transactionRepository = transactionRepository;
        this.transactionManager = transactionManager;
    }

    @Override
    @Transactional
    public Category createCategory(String name, String description) {
        Category category = new Category();
        category.setName(name);
        category.setDescription(description);
        return categoryRepository.save(category);
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        return categoryRepository.findById(id);
    }

    @Override
    public Optional<Category> getCategoryByName(String name) {
        return categoryRepository.findByName(name);
    }

    @Override
    public Iterable<Category> getAllCategories() {
        return categoryRepository.findAll();
    }

    @Override
    @Transactional
    public Category updateCategory(Long id, String name, String description) {
        Category category = categoryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        category.setName(name);
        category.setDescription(description);

        return categoryRepository.save(category);
    }

    @Override
    public void deleteCategory(Long id) {
        TransactionStatus status = transactionManager.getTransaction(new DefaultTransactionDefinition());
        try {
            // Получение категории по id
            Category category = categoryRepository.findById(id)
                    .orElseThrow(() -> new IllegalArgumentException("Category not found"));

            // Удаление всех транзакций, связанных с этой категорией
            List<Transaction> transactions = transactionRepository.getTransactionsByCategory(category);
            for (Transaction transaction : transactions) {
                transactionRepository.delete(transaction);
            }

            // Удаление самой категории
            categoryRepository.deleteById(id);

            // Фиксация транзакции
            transactionManager.commit(status);
        } catch (DataAccessException ex) {
            // Откатить транзакцию в случае ошибки
            transactionManager.rollback(status);
            throw ex; // rethrow the exception for further handling if needed
        }
    }
}
