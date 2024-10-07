package ru.MaslovArtemy.NauJava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import ru.MaslovArtemy.NauJava.model.Category;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.model.User;

import java.util.Date;
import java.util.List;

public interface TransactionRepository extends CrudRepository<Transaction, Long> {
    List<Transaction> getTransactionsByCategory(Category category);
    @Query("SELECT t FROM Transaction t WHERE t.date = :date AND t.user = :user")
    List<Transaction> getTransactionsByDateAndUser(@Param("date") Date date, @Param("user") User user);
}
