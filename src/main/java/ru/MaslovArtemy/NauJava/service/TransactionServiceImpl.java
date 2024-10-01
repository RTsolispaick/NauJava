package ru.MaslovArtemy.NauJava.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.repository.TransactionRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

@Service
public class TransactionServiceImpl implements TransactionService {
    private final TransactionRepository transactionRepository;

    @Autowired
    public TransactionServiceImpl(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void createTransaction(Double amount, String category, LocalDate date, Transaction.Type type, String description) {
        transactionRepository.create(new Transaction(null, amount,
                category,
                date,
                type,
                description));
    }

    @Override
    public Optional<Transaction> findById(Long id) {
        return transactionRepository.read(id);
    }

    @Override
    public Optional<List<Transaction>> getAll() {
        return transactionRepository.readAll();
    }

    @Override
    public Optional<List<Transaction>> findByFilter(Predicate<Transaction> predicate) {
        Optional<List<Transaction>> optionalTransactions = transactionRepository.readAll();

        return optionalTransactions
                .map(transactions -> transactions.stream()
                        .filter(predicate)
                        .collect(Collectors.toList()))
                .filter(filteredTransactions -> !filteredTransactions.isEmpty());
    }

    @Override
    public void deleteById(Long id) throws IllegalStateException {
        transactionRepository.delete(id);
    }
}
