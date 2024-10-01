package ru.MaslovArtemy.NauJava.repository;

import java.util.List;
import java.util.Optional;

/**
 * Интерфейс для базового репозитория CRUD (Создание, Чтение, Обновление, Удаление).
 *
 * @param <T> тип сущности, которую управляет репозиторий
 * @param <ID> тип идентификатора сущности
 */
public interface CrudRepository<T, ID> {

    /**
     * Создает новую сущность в репозитории.
     *
     * @param entity сущность для создания
     */
    void create(T entity);

    /**
     * Читает сущность из репозитория по ее идентификатору.
     *
     * @param id идентификатор сущности для чтения
     * @return {@link Optional}, содержащий сущность, если она найдена, или пустой {@link Optional}, если не найдена
     */
    Optional<T> read(ID id);

    /**
     * Читает все сущности из репозитория.
     *
     * @return {@link Optional}, содержащий список сущностей, если таковые существуют, или пустой {@link Optional}, если их нет
     */
    Optional<List<T>> readAll();

    /**
     * Обновляет существующую сущность в репозитории.
     *
     * @param entity сущность с обновленной информацией
     * @throws IllegalStateException если сущность не существует в репозитории
     */
    void update(T entity) throws IllegalStateException;

    /**
     * Удаляет сущность из репозитория по ее идентификатору.
     *
     * @param id идентификатор сущности для удаления
     * @throws IllegalStateException если сущность не существует в репозитории
     */
    void delete(ID id) throws IllegalStateException;
}
