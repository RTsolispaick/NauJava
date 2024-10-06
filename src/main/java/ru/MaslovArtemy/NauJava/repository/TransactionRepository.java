package ru.MaslovArtemy.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.MaslovArtemy.NauJava.model.Transaction;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
}
