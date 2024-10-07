package ru.MaslovArtemy.NauJava.service;

import ru.MaslovArtemy.NauJava.model.Category;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.model.User;

import java.util.Date;
import java.util.Optional;

public interface TransactionService {
    Transaction createTransaction(Float amount, Date date, String description, String type, User user, String budget, String category);

    Optional<Transaction> getTransactionById(Long id);

    Iterable<Transaction> getAllTransactions();

    Transaction updateTransaction(Long id, Float amount, Date date, String description, String type);

    void deleteTransaction(Long id);
}
