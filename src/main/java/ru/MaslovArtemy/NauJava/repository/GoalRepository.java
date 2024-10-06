package ru.MaslovArtemy.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import ru.MaslovArtemy.NauJava.model.Goal;

public interface GoalRepository extends CrudRepository<Goal, Long> {
}
