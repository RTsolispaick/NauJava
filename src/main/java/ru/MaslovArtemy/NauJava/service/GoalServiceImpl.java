package ru.MaslovArtemy.NauJava.service;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.MaslovArtemy.NauJava.model.Goal;
import ru.MaslovArtemy.NauJava.model.User;
import ru.MaslovArtemy.NauJava.repository.GoalRepository;
import ru.MaslovArtemy.NauJava.repository.UserRepository;

import java.util.Date;
import java.util.Optional;

@Service
public class GoalServiceImpl implements GoalService {

    private final GoalRepository goalRepository;
    private final UserRepository userRepository;

    @Autowired
    public GoalServiceImpl(GoalRepository goalRepository, UserRepository userRepository) {
        this.goalRepository = goalRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public Goal createGoal(String name, Float targetAmount, Date deadline, Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Goal goal = new Goal(name, targetAmount, deadline, user);
        return goalRepository.save(goal);
    }

    @Override
    public Optional<Goal> getGoalById(Long id) {
        return goalRepository.findById(id);
    }

    @Override
    public Iterable<Goal> getAllGoals() {
        return goalRepository.findAll();
    }

    @Override
    @Transactional
    public Goal updateGoal(Long id, String name, Float targetAmount, Date deadline) {
        Goal goal = goalRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Goal not found"));

        goal.setName(name);
        goal.setTargetAmount(targetAmount);
        goal.setDeadline(deadline);

        return goalRepository.save(goal);
    }

    @Override
    @Transactional
    public void deleteGoal(Long id) {
        goalRepository.deleteById(id);
    }
}
