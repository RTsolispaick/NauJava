package ru.MaslovArtemy.NauJava.service;

import ru.MaslovArtemy.NauJava.model.Budget;
import ru.MaslovArtemy.NauJava.model.Transaction;

import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface BudgetService {

    Budget createBudget(String name, Float totalAmount, Date startDate, Date endDate, Long userId);

    Optional<Budget> getBudgetById(Long id);

    Iterable<Budget> getAllBudgets();

    Budget updateBudget(Long id, String name, Float totalAmount, Date startDate, Date endDate);

    void deleteBudget(Long id);

    List<Transaction> getTransactionsByBudgetId(Long budgetId);
}
