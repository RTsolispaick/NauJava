package ru.MaslovArtemy.NauJava.repository.custom;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import ru.MaslovArtemy.NauJava.model.Transaction;

@Repository
public interface TransactionRepositoryCriteria extends CrudRepository<Transaction, Long>, CustomTransactionRepository {
}
