package ru.MaslovArtemy.NauJava.service;

import ru.MaslovArtemy.NauJava.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public interface TransactionService {
    void createTransaction(Double amount, String category, LocalDate date, Transaction.Type type, String description);

    Optional<Transaction> findById(Long id);

    Optional<List<Transaction>> getAll();

    Optional<List<Transaction>> findByFilter(Predicate<Transaction> predicate);

    void deleteById(Long id);


}
