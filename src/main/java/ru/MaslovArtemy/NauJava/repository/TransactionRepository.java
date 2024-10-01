package ru.MaslovArtemy.NauJava.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import ru.MaslovArtemy.NauJava.model.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Component
public class TransactionRepository implements CrudRepository<Transaction, Long> {
    private final HashMap<Long, Transaction> transactionContainer;
    private Long nextId;

    @Autowired
    public TransactionRepository(HashMap<Long, Transaction> transactionContainer) {
        this.transactionContainer = transactionContainer;
        this.nextId = (long) transactionContainer.size();
    }

    @Override
    public void create(Transaction transaction) {
        transactionContainer.put(nextId, transaction.setId(nextId++));
    }

    @Override
    public Optional<Transaction> read(Long id) {
        return Optional.ofNullable(transactionContainer.get(id));
    }

    @Override
    public Optional<List<Transaction>> readAll() {
        return Optional.of(new ArrayList<>(transactionContainer.values()));
    }

    @Override
    public void update(Transaction transaction) throws IllegalStateException {
        if (transactionContainer.get(transaction.id()) == null) {
            throw new IllegalStateException("Транзакции с индексом " + transaction.id() + " не существует!");
        }
        transactionContainer.put(transaction.id(), transaction);
    }

    @Override
    public void delete(Long id) throws IllegalStateException {
        if (transactionContainer.remove(id) == null) {
            throw new IllegalStateException("Транзакции с индексом " + id + " не существует!");
        }
    }
}
