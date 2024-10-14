package ru.MaslovArtemy.NauJava.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import ru.MaslovArtemy.NauJava.model.Budget;

import java.util.Optional;

@RepositoryRestResource(path = "budget")
public interface BudgetRepository extends CrudRepository<Budget, Long> {
    Optional<Budget> findByName(String name);
}
