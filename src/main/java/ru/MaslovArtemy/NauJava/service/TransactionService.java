package ru.MaslovArtemy.NauJava.service;

import ru.MaslovArtemy.NauJava.model.Transaction;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * Интерфейс для сервиса управления транзакциями.
 */
public interface TransactionService {

    /**
     * Создает новую транзакцию.
     *
     * @param amount     сумма транзакции
     * @param category   категория транзакции
     * @param date       дата транзакции
     * @param type       тип транзакции (прибыль или расход)
     * @param description описание транзакции
     */
    void createTransaction(Double amount, String category, LocalDate date, Transaction.Type type, String description);

    /**
     * Находит транзакцию по её идентификатору.
     *
     * @param id идентификатор транзакции
     * @return {@link Optional}, содержащий найденную транзакцию, или пустой {@link Optional}, если транзакция не найдена
     */
    Optional<Transaction> findById(Long id);

    /**
     * Получает все транзакции.
     *
     * @return {@link Optional}, содержащий список всех транзакций, если таковые существуют, или пустой {@link Optional}, если их нет
     */
    Optional<List<Transaction>> getAll();

    /**
     * Находит транзакции по заданному фильтру.
     *
     * @param predicate предикат, определяющий условия фильтрации
     * @return {@link Optional}, содержащий список транзакций, соответствующих фильтру, или пустой {@link Optional}, если таковые не найдены
     */
    Optional<List<Transaction>> findByFilter(Predicate<Transaction> predicate);

    /**
     * Удаляет транзакцию по её идентификатору.
     *
     * @param id идентификатор транзакции для удаления
     */
    void deleteById(Long id);
}
