package ru.MaslovArtemy.NauJava.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.MaslovArtemy.NauJava.model.Budget;
import ru.MaslovArtemy.NauJava.model.Category;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.model.User;
import ru.MaslovArtemy.NauJava.repository.BudgetRepository;
import ru.MaslovArtemy.NauJava.repository.CategoryRepository;
import ru.MaslovArtemy.NauJava.repository.TransactionRepository;
import ru.MaslovArtemy.NauJava.repository.UserRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, UserRepository userRepository,
                                  BudgetRepository budgetRepository, CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Transaction createTransaction(Float amount, Date date, String description, String type, Long userId, Long budgetId, Long categoryId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // Поиск бюджета по budgetId
        Budget budget = budgetRepository.findById(budgetId)
                .orElseThrow(() -> new IllegalArgumentException("Budget not found"));

        // Поиск категории по categoryId
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        // Создание и сохранение транзакции
        Transaction transaction = new Transaction(amount, date, description, type, user, budget, category);
        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public Iterable<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    @Transactional
    public Transaction updateTransaction(Long id, Float amount, Date date, String description, String type) {
        Transaction transaction = transactionRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Transaction not found"));

        transaction.setAmount(amount);
        transaction.setDate(date);
        transaction.setDescription(description);
        transaction.setType(type);

        return transactionRepository.save(transaction);
    }

    @Override
    @Transactional
    public void deleteTransaction(Long id) {
        transactionRepository.deleteById(id);
    }
}
