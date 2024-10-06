package ru.MaslovArtemy.NauJava.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import ru.MaslovArtemy.NauJava.model.Budget;
import ru.MaslovArtemy.NauJava.model.Transaction;

import java.util.List;

public interface BudgetRepository extends CrudRepository<Budget, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.budget.id = :budgetId")
    List<Transaction> findTransactionsByBudgetId(Long budgetId);
}
