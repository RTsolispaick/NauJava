package ru.MaslovArtemy.NauJava.model;

import java.time.LocalDate;

/**
 * Представляет транзакцию для учета доходов и расходов пользователя.
 * Транзакция содержит информацию о сумме, категории, дате, типе и описании.
 * Класс является immutable благодаря использованию record.
 *
 * @param id          уникальный идентификатор транзакции
 * @param amount      сумма транзакции
 * @param category    категория, к которой относится транзакция
 * @param date        дата, когда была проведена транзакция
 * @param type        тип транзакции (доход или расход)
 * @param description описание транзакции
 */
public record Transaction(Long id, Double amount, String category, LocalDate date, Type type, String description) {

    /**
     * Типы транзакций: доход или расход.
     * Используется для обозначения положительных и отрицательных изменений в финансах.
     */
    public enum Type {
        /**
         * Доходная транзакция.
         * Представляет положительное изменение в финансах.
         */
        INCOME("+", 1),

        /**
         * Расходная транзакция.
         * Представляет отрицательное изменение в финансах.
         */
        EXPENSE("-", -1);

        private final String str;
        private final Integer integer;

        /**
         * Конструктор для типа транзакции.
         *
         * @param str символ, представляющий тип транзакции
         * @param i   числовое представление типа (1 для доходов, -1 для расходов)
         */
        Type(String str, int i) {
            this.str = str;
            this.integer = i;
        }

        /**
         * Получает символ, представляющий тип транзакции.
         *
         * @return строковое представление типа ("+" для доходов, "-" для расходов)
         */
        public String getStr() {
            return str;
        }

        /**
         * Получает числовое представление типа транзакции.
         *
         * @return 1 для доходов, -1 для расходов
         */
        public Integer getInteger() {
            return integer;
        }

        /**
         * Преобразует строку в соответствующий тип транзакции.
         *
         * @param str строка, представляющая тип транзакции
         * @return соответствующий {@code Type} (INCOME или EXPENSE)
         * @throws IllegalArgumentException если строка не соответствует ни одному типу
         */
        public static Type fromString(String str) {
            for (Type type : Type.values()) {
                if (type.getStr().equals(str)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Неизвестный тип: " + str);
        }
    }

    /**
     * Создает новую транзакцию с обновленным идентификатором.
     *
     * @param id новый уникальный идентификатор транзакции
     * @return новая транзакция с обновленным идентификатором
     */
    public Transaction setId(Long id) {
        return new Transaction(id, amount, category, date, type, description);
    }
}
