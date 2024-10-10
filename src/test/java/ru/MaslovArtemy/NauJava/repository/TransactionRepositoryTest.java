package ru.MaslovArtemy.NauJava.repository;

import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.MaslovArtemy.NauJava.model.Category;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.model.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
public class TransactionRepositoryTest {
    private final TransactionRepository transactionRepository;
    private final EntityManager entityManager;

    @Autowired
    public TransactionRepositoryTest(TransactionRepository transactionRepository, EntityManager entityManager) {
        this.transactionRepository = transactionRepository;
        this.entityManager = entityManager;
    }

    /**
     * Проверяет получение транзакций по категории.
     */
    @Test
    public void testGetTransactionsByCategory() {
        Category category1 = new Category();
        category1.setName("Test Category");
        entityManager.persist(category1);

        Category category2 = new Category();
        category2.setName("Test Category1");
        entityManager.persist(category2);

        Transaction transaction1 = new Transaction();
        transaction1.setCategory(category1);
        transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setCategory(category2);
        transactionRepository.save(transaction2);

        List<Transaction> expectedTransactions = List.of(transaction1);

        List<Transaction> actualTransactions = transactionRepository.getTransactionsByCategory(category1);

        assertEquals(expectedTransactions, actualTransactions);
    }

    /**
     * Проверяет получение транзакций по дате и пользователю.
     */
    @Test
    public void testGetTransactionsByDateAndUser() {
        User user1 = new User();
        user1.setName("Test User1");
        entityManager.persist(user1);

        User user2 = new User();
        user2.setName("Test User2");
        entityManager.persist(user2);

        Date date = new Date();

        Transaction transaction1 = new Transaction();
        transaction1.setUser(user1);
        transaction1.setDate(date);
        transactionRepository.save(transaction1);

        Transaction transaction2 = new Transaction();
        transaction2.setUser(user2);
        transaction2.setDate(date);
        transactionRepository.save(transaction2);

        Transaction transaction3 = new Transaction();
        transaction3.setUser(user1);
        transaction3.setDate(date);
        transactionRepository.save(transaction3);

        List<Transaction> expectedTransactions = Arrays.asList(transaction1, transaction3);

        List<Transaction> actualTransactions = transactionRepository.getTransactionsByDateAndUser(date, user1);

        assertEquals(expectedTransactions, actualTransactions);
    }

    /**
     * Проверяет поведение метода получения транзакций по категории, когда транзакций нет.
     */
    @Test
    public void testGetTransactionsByCategory_NoTransactions() {
        Category category = new Category();
        category.setName("Empty Category");
        entityManager.persist(category);

        List<Transaction> actualTransactions = transactionRepository.getTransactionsByCategory(category);

        assertEquals(Collections.emptyList(), actualTransactions);
    }

    /**
     * Проверяет поведение метода получения транзакций по дате и пользователю, когда транзакций нет.
     */
    @Test
    public void testGetTransactionsByDateAndUser_NoTransactions() {
        User user = new User();
        user.setName("Non-existent User");
        entityManager.persist(user);

        Date date = new Date();

        List<Transaction> actualTransactions = transactionRepository.getTransactionsByDateAndUser(date, user);

        assertEquals(Collections.emptyList(), actualTransactions);
    }
}
