package ru.MaslovArtemy.NauJava.service;

import ru.MaslovArtemy.NauJava.model.Goal;

import java.util.Date;
import java.util.Optional;

public interface GoalService {
    Goal createGoal(String name, Float targetAmount, Date deadline, Long userId);

    Optional<Goal> getGoalById(Long id);

    Iterable<Goal> getAllGoals();

    Goal updateGoal(Long id, String name, Float targetAmount, Date deadline);

    void deleteGoal(Long id);
}
