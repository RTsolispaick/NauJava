package ru.MaslovArtemy.NauJava.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Setter @Getter
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String name;

    private String password;

    @Temporal(TemporalType.DATE)
    private Date registrationDate;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Budget> budgets;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Goal> goals;

    @OneToMany(mappedBy = "user")
    @JsonIgnore
    private List<Transaction> transactions;

    public User() {}

    public User(String name, String password, Date registrationDate) {
        this.name = name;
        this.password = password;
        this.registrationDate = registrationDate;
    }
}

