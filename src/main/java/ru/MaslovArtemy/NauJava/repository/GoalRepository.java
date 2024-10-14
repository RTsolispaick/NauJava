package ru.MaslovArtemy.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.MaslovArtemy.NauJava.model.Goal;

@RepositoryRestResource(path = "goal")
public interface GoalRepository extends CrudRepository<Goal, Long> {
}
