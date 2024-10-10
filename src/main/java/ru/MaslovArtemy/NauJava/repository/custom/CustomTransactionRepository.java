package ru.MaslovArtemy.NauJava.repository.custom;

import ru.MaslovArtemy.NauJava.model.Category;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.model.User;

import java.util.Date;
import java.util.List;

public interface CustomTransactionRepository {
    List<Transaction> findTransactionsByDateAndUser(Date date, User user);

    List<Transaction> findTransactionsByCategory(Category category);
}
