package ru.MaslovArtemy.NauJava.model;

import java.time.LocalDate;

public class Transaction
{
    public enum Type {
        INCOME("+", 1),
        EXPENSE("-", -1);

        private final String str;
        private final Integer integer;

        Type(String str, int i) {
            this.str = str;
            this.integer = i;
        }

        public String getStr() {
            return str; // Метод для получения значения строки
        }

        public Integer getInteger() {
            return integer; // Метод для получения значения строки
        }

        public static Type fromString(String str) {
            for (Type type : Type.values()) {
                if (type.getStr().equals(str)) {
                    return type;
                }
            }
            throw new IllegalArgumentException("Неизвестный тип: " + str);
        }
    }

    private Long id;
    private double amount;
    private String category;
    private LocalDate date;
    private Type type; // "INCOME" или "EXPENSE"
    private String description;

    public Transaction(Double amount, String category, LocalDate date, Type type, String description) {
        this.amount = amount;
        this.category = category;
        this.date = date;
        this.type = type;
        this.description = description;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Type getType() {
        return type;
    }

    public void setType(String type) {
        this.type = Type.fromString(type);
    }
}
