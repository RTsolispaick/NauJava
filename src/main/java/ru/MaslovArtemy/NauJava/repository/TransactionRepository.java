package ru.MaslovArtemy.NauJava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.MaslovArtemy.NauJava.model.Category;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.model.User;

import java.util.Date;
import java.util.List;

@RepositoryRestResource(path = "transaction")
public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.category = :category")
    List<Transaction> getTransactionsByCategory(Category category);
    List<Transaction> getTransactionsByDateAndUser(Date date, User user);
}
