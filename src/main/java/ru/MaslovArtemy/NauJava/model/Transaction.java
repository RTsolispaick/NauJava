package ru.MaslovArtemy.NauJava.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter @Getter
@Entity
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Float amount;

    @Temporal(TemporalType.DATE)
    private Date date;

    private String description;
    private String type;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "budget_id")
    private Budget budget;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    public Transaction() {}

    public Transaction(Float amount, Date date, String description, String type, User user, Budget budget, Category category) {
        this.amount = amount;
        this.date = date;
        this.description = description;
        this.type = type;
        this.user = user;
        this.budget = budget;
        this.category = category;
    }
}
