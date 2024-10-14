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

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final TransactionRepository transactionRepository;
    private final BudgetRepository budgetRepository;
    private final CategoryRepository categoryRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository, BudgetRepository budgetRepository,
                                  CategoryRepository categoryRepository) {
        this.transactionRepository = transactionRepository;
        this.budgetRepository = budgetRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public Transaction createTransaction(Float amount, Date date, String description, String type, User user, String budget, String category) {
        Budget budget1 = budgetRepository.findByName(budget)
                .orElseThrow(() -> new IllegalArgumentException("Budget not found"));

        Category category1 = categoryRepository.findByName(category)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        Transaction transaction = new Transaction(amount, date, description, type, user, budget1, category1);
        return transactionRepository.save(transaction);
    }

    @Override
    public Optional<Transaction> getTransactionById(Long id) {
        return transactionRepository.findById(id);
    }

    @Override
    public List<Transaction> getTransactionsByCategory(Category category) {
        return transactionRepository.getTransactionsByCategory(category);
    }

    @Override
    public List<Transaction> getTransactionsByDateAndUser(Date date, User user) {
        return transactionRepository.getTransactionsByDateAndUser(date, user);
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
