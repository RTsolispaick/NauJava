package ru.MaslovArtemy.NauJava.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.MaslovArtemy.NauJava.model.Budget;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.model.User;
import ru.MaslovArtemy.NauJava.repository.BudgetRepository;
import ru.MaslovArtemy.NauJava.repository.UserRepository;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Service
public class BudgetServiceImpl implements BudgetService {

    private final BudgetRepository budgetRepository;
    private final UserRepository userRepository;

    @Autowired
    public BudgetServiceImpl(BudgetRepository budgetRepository, UserRepository userRepository) {
        this.budgetRepository = budgetRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Budget createBudget(String name, Float totalAmount, Date startDate, Date endDate, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Budget budget = new Budget(name, totalAmount, startDate, endDate, user);
        return budgetRepository.save(budget);
    }

    @Override
    public Optional<Budget> getBudgetById(Long id) {
        return budgetRepository.findById(id);
    }

    @Override
    public Iterable<Budget> getAllBudgets() {
        return budgetRepository.findAll();
    }

    @Override
    @Transactional
    public Budget updateBudget(Long id, String name, Float totalAmount, Date startDate, Date endDate) {
        Budget budget = budgetRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Budget not found"));

        budget.setName(name);
        budget.setTotalAmount(totalAmount);
        budget.setStartDate(startDate);
        budget.setEndDate(endDate);

        return budgetRepository.save(budget);
    }

    @Override
    @Transactional
    public void deleteBudget(Long id) {
        budgetRepository.deleteById(id);
    }
}
