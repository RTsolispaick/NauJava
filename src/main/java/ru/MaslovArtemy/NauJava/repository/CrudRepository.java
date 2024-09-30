package ru.MaslovArtemy.NauJava.repository;

import java.util.List;
import java.util.Optional;

public interface CrudRepository<T, ID> {
    void create(T entity);

    Optional<T> read(ID id);

    Optional<List<T>> readAll();

    void update(T entity) throws IllegalStateException;

    void delete(ID id) throws IllegalStateException;
}

