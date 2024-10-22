package ru.MaslovArtemy.NauJava.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter @Getter
@Entity
public class Budget {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;
    private Float totalAmount;

    @Temporal(TemporalType.DATE)
    private Date startDate;

    @Temporal(TemporalType.DATE)
    private Date endDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @JsonIgnore
    private User user;

    @OneToMany(mappedBy = "budget", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Transaction> transactions;

    // Пустой конструктор
    public Budget() {}

    // Конструктор с параметрами
    public Budget(String name, Float totalAmount, Date startDate, Date endDate, User user) {
        this.name = name;
        this.totalAmount = totalAmount;
        this.startDate = startDate;
        this.endDate = endDate;
        this.user = user;
    }
}
