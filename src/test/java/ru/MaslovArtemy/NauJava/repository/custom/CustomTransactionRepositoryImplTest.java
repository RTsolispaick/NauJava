package ru.MaslovArtemy.NauJava.repository.custom;

import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.MaslovArtemy.NauJava.model.Category;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.model.User;

import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class CustomTransactionRepositoryImplTest {

    @Mock
    private EntityManager entityManager;

    @Mock
    private CriteriaBuilder criteriaBuilder;

    @Mock
    private CriteriaQuery<Transaction> criteriaQuery;

    @Mock
    private Root<Transaction> root;

    @InjectMocks
    private CustomTransactionRepositoryImpl customTransactionRepository;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        when(entityManager.getCriteriaBuilder()).thenReturn(criteriaBuilder);
        when(criteriaBuilder.createQuery(Transaction.class)).thenReturn(criteriaQuery);
        when(criteriaQuery.from(Transaction.class)).thenReturn(root);
    }

    /**
     * Тестирует получение транзакций по дате и пользователю.
     */
    @Test
    public void testFindTransactionsByDateAndUser() {
        User user = new User();
        Date date = new Date();
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        List<Transaction> expectedTransactions = Arrays.asList(transaction1, transaction2);

        Predicate datePredicate = criteriaBuilder.equal(root.get("date"), date);
        Predicate userPredicate = criteriaBuilder.equal(root.get("user"), user);

        setupQueryWithPredicates(new Predicate[]{datePredicate, userPredicate}, expectedTransactions);

        List<Transaction> actualTransactions = customTransactionRepository.findTransactionsByDateAndUser(date, user);

        assertEquals(expectedTransactions, actualTransactions);
    }

    /**
     * Тестирует получение транзакций по категории.
     */
    @Test
    public void testFindTransactionsByCategory() {
        Category category = new Category();
        Transaction transaction1 = new Transaction();
        Transaction transaction2 = new Transaction();
        List<Transaction> expectedTransactions = Arrays.asList(transaction1, transaction2);

        Predicate categoryPredicate = criteriaBuilder.equal(root.get("category"), category);

        setupQueryWithPredicate(categoryPredicate, expectedTransactions);

        List<Transaction> actualTransactions = customTransactionRepository.findTransactionsByCategory(category);

        assertEquals(expectedTransactions, actualTransactions);
    }

    /**
     * Тестирует получение транзакций по дате и пользователю, когда пользователь не найден.
     */
    @Test
    public void testFindTransactionsByDateAndUser_UserNotFound() {
        User user = new User();
        Date date = new Date();

        Predicate datePredicate = criteriaBuilder.equal(root.get("date"), date);
        Predicate userPredicate = criteriaBuilder.equal(root.get("user"), user);

        setupQueryWithPredicates(new Predicate[]{datePredicate, userPredicate}, Collections.emptyList());

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

        Predicate datePredicate = criteriaBuilder.equal(root.get("date"), date);
        Predicate userPredicate = criteriaBuilder.equal(root.get("user"), user);

        setupQueryWithPredicates(new Predicate[]{datePredicate, userPredicate}, Collections.emptyList());

        List<Transaction> actualTransactions = customTransactionRepository.findTransactionsByDateAndUser(date, user);

        assertEquals(Collections.emptyList(), actualTransactions);
    }

    /**
     * Тестирует получение транзакций по категории, когда нет транзакций.
     */
    @Test
    public void testFindTransactionsByCategory_NoTransactions() {
        Category category = new Category();

        Predicate categoryPredicate = criteriaBuilder.equal(root.get("category"), category);

        setupQueryWithPredicate(categoryPredicate, Collections.emptyList());

        List<Transaction> actualTransactions = customTransactionRepository.findTransactionsByCategory(category);

        assertEquals(Collections.emptyList(), actualTransactions);
    }

    private void setupQueryWithPredicates(Predicate[] predicates, List<Transaction> expectedTransactions) {
        when(criteriaQuery.where(predicates)).thenReturn(criteriaQuery);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);

        TypedQuery<Transaction> typedQuery = mock(TypedQuery.class);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedTransactions);
    }

    private void setupQueryWithPredicate(Predicate predicate, List<Transaction> expectedTransactions) {
        when(criteriaQuery.where(predicate)).thenReturn(criteriaQuery);
        when(criteriaQuery.select(root)).thenReturn(criteriaQuery);

        TypedQuery<Transaction> typedQuery = mock(TypedQuery.class);
        when(entityManager.createQuery(criteriaQuery)).thenReturn(typedQuery);
        when(typedQuery.getResultList()).thenReturn(expectedTransactions);
    }
}
