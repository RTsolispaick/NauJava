package ru.MaslovArtemy.NauJava.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.MaslovArtemy.NauJava.model.Budget;
import ru.MaslovArtemy.NauJava.model.Category;
import ru.MaslovArtemy.NauJava.model.DTO.TransactionDTO;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.model.User;
import ru.MaslovArtemy.NauJava.repository.BudgetRepository;
import ru.MaslovArtemy.NauJava.repository.CategoryRepository;
import ru.MaslovArtemy.NauJava.repository.TransactionRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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
    public Transaction createTransaction(Double amount, Date date, String description, String type, User user, String budget, String category) {
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
    public List<TransactionDTO> getTransactionsByCategory(Category category) {
        return transactionRepository.getTransactionsByCategory(category).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public List<TransactionDTO> getTransactionsByDateAndUser(Date date, User user) {
        return transactionRepository.getTransactionsByDateAndUser(date, user).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Override
    public Iterable<Transaction> getAllTransactions() {
        return transactionRepository.findAll();
    }

    @Override
    @Transactional
    public Transaction updateTransaction(Long id, Double amount, Date date, String description, String type) {
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

    private TransactionDTO convertToDTO(Transaction transaction) {
        return new TransactionDTO(
                transaction.getId(),
                transaction.getAmount(),
                transaction.getDate(),
                transaction.getDescription(),
                transaction.getType(),
                transaction.getUser().getId(),
                transaction.getBudget().getId(),
                transaction.getCategory().getId()
        );
    }
}
