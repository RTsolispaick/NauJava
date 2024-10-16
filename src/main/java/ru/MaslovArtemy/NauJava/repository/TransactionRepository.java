package ru.MaslovArtemy.NauJava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.MaslovArtemy.NauJava.model.Category;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.model.User;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> getTransactionsByCategory(Category category);
    @Query("SELECT t FROM Transaction t JOIN t.user u WHERE t.date = :date AND u = :user")
    List<Transaction> getTransactionsByDateAndUser(Date date, User user);
}
