package ru.MaslovArtemy.NauJava.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.dao.DataAccessException;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionStatus;
import ru.MaslovArtemy.NauJava.model.Category;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.repository.CategoryRepository;
import ru.MaslovArtemy.NauJava.repository.TransactionRepository;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class CategoryServiceTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private TransactionRepository transactionRepository;

    @Mock
    private PlatformTransactionManager transactionManager;

    @InjectMocks
    private CategoryServiceImpl categoryService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Тестирует успешное удаление категории.
     */
    @Test
    public void testDeleteCategory_Success() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);

        Transaction transaction = new Transaction();
        transaction.setCategory(category);

        TransactionStatus transactionStatus = mock(TransactionStatus.class);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(transactionRepository.getTransactionsByCategory(category)).thenReturn(List.of(transaction));
        when(transactionManager.getTransaction(any())).thenReturn(transactionStatus);

        categoryService.deleteCategory(categoryId);

        verify(transactionRepository).delete(transaction);
        verify(categoryRepository).deleteById(categoryId);
        verify(transactionManager).commit(transactionStatus);
    }

    /**
     * Тестирует попытку удаления категории, которая не найдена.
     */
    @Test
    public void testDeleteCategoryCategoryNotFound() {
        Long nonExistentCategoryId = 999L;

        when(categoryRepository.findById(nonExistentCategoryId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> categoryService.deleteCategory(nonExistentCategoryId));

        verify(transactionManager, never()).commit(any(TransactionStatus.class));
        verify(transactionManager, never()).rollback(any(TransactionStatus.class));
    }

    /**
     * Тестирует удаление категории, когда возникает ошибка при удалении транзакции.
     */
    @Test
    public void testDeleteCategoryTransactionDeleteError() {
        Long categoryId = 1L;
        Category category = new Category();
        category.setId(categoryId);

        TransactionStatus transactionStatus = mock(TransactionStatus.class);

        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(transactionRepository.getTransactionsByCategory(category)).thenReturn(List.of(new Transaction()));
        when(transactionManager.getTransaction(any())).thenReturn(transactionStatus);

        doThrow(new DataAccessException("Error deleting transaction") {}).when(transactionRepository).delete(any(Transaction.class));

        assertThrows(DataAccessException.class, () -> categoryService.deleteCategory(categoryId));

        verify(transactionManager, never()).commit(any(TransactionStatus.class));
        verify(transactionManager).rollback(transactionStatus);
    }
}
