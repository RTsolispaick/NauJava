package ru.MaslovArtemy.NauJava.repository.custom;

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
public class CustomTransactionRepositoryImplTest {
    private final TransactionRepositoryCriteria customTransactionRepository;
    private final EntityManager entityManager;

    @Autowired
    public CustomTransactionRepositoryImplTest(TransactionRepositoryCriteria customTransactionRepository, EntityManager entityManager) {
        this.customTransactionRepository = customTransactionRepository;
        this.entityManager = entityManager;
    }

    /**
     * Тестирует получение транзакций по дате и пользователю.
     */
    @Test
    public void testFindTransactionsByDateAndUser() {
        Date date1 = new Date();
        User user1 = new User("Artem", "12345", date1);
        entityManager.persist(user1);

        Date date2 = new Date(123);
        User user2 = new User("Vova", "54321", date2);
        entityManager.persist(user2);

        List<Transaction> transactions = Arrays.asList(
                new Transaction(0.0f, date1, "some description", "+", user1, null, null),
                new Transaction(0.0f, date2, "some description", "-", user2, null, null),
                new Transaction(0.0f, date1, "some description", "+", user1, null, null),
                new Transaction(0.0f, date2, "some description", "-", user1, null, null),
                new Transaction(0.0f, date1, "some description", "-", user2, null, null)
        );

        customTransactionRepository.saveAll(transactions);

        List<Transaction> expectedTransactions = Arrays.asList(transactions.get(0), transactions.get(2));

        List<Transaction> actualTransactions = customTransactionRepository.findTransactionsByDateAndUser(date1, user1);

        assertEquals(expectedTransactions, actualTransactions);
    }

    /**
     * Тестирует получение транзакций по категории.
     */
    @Test
    public void testFindTransactionsByCategory() {
        Category category1 = new Category("some1", "description1");
        entityManager.persist(category1);
        Category category2 = new Category("some2", "description2");
        entityManager.persist(category2);

        List<Transaction> transactions = Arrays.asList(
                new Transaction(0.0f, new Date(), "desc1", "+", null, null, category1),
                new Transaction(0.0f, new Date(), "desc2", "-", null, null, category2),
                new Transaction(0.0f, new Date(), "desc3", "+", null, null, category1)
        );

        customTransactionRepository.saveAll(transactions);

        List<Transaction> expectedTransactions = Arrays.asList(transactions.get(0), transactions.get(2));

        List<Transaction> actualTransactions = customTransactionRepository.findTransactionsByCategory(category1);

        assertEquals(expectedTransactions, actualTransactions);
    }

    /**
     * Тестирует получение транзакций по дате и пользователю, когда пользователь не найден.
     */
    @Test
    public void testFindTransactionsByDateAndUser_UserNotFound() {
        User user = new User();
        entityManager.persist(user);

        Date date = new Date();

        User user2 = new User();
        entityManager.persist(user2);

        List<Transaction> transactions = Arrays.asList(
                new Transaction(0.0f, new Date(), "desc1", "+", user2, null, null),
                new Transaction(0.0f, new Date(), "desc2", "-", user2, null, null),
                new Transaction(0.0f, new Date(), "desc3", "+", user2, null, null)
        );

        customTransactionRepository.saveAll(transactions);

        List<Transaction> actualTransactions = customTransactionRepository.findTransactionsByDateAndUser(date, user);

        assertEquals(Collections.emptyList(), actualTransactions);
    }

    /**
     * Тестирует получение транзакций по дате и пользователю, когда пользователь не найден.
     */
    @Test
    public void testFindTransactionsByDateAndUser_DateNotFound() {
        User user = new User();
        entityManager.persist(user);

        Date date = new Date();
        Date date2 = new Date(123);

        List<Transaction> transactions = Arrays.asList(
                new Transaction(0.0f, date2, "desc1", "+", user, null, null),
                new Transaction(0.0f, date2, "desc2", "-", user, null, null),
                new Transaction(0.0f, date2, "desc3", "+", user, null, null)
        );

        customTransactionRepository.saveAll(transactions);

        List<Transaction> actualTransactions = customTransactionRepository.findTransactionsByDateAndUser(date, user);

        assertEquals(Collections.emptyList(), actualTransactions);
    }

    /**
     * Тестирует получение транзакций по дате и пользователю, когда нет транзакций.
     */
    @Test
    public void testFindTransactionsByDateAndUser_NoTransactions() {
        User user = new User();
        Date date = new Date();
        entityManager.persist(user);

        List<Transaction> actualTransactions = customTransactionRepository.findTransactionsByDateAndUser(date, user);

        assertEquals(Collections.emptyList(), actualTransactions);
    }

    /**
     * Тестирует получение транзакций по категории, когда нет транзакций.
     */
    @Test
    public void testFindTransactionsByCategory_NoTransactions() {
        Category category = new Category();
        entityManager.persist(category);

        List<Transaction> actualTransactions = customTransactionRepository.findTransactionsByCategory(category);

        assertEquals(Collections.emptyList(), actualTransactions);
    }
}
