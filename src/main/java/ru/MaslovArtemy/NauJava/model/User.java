package ru.MaslovArtemy.NauJava.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Setter @Getter
@Entity
@Table(name = "app_user")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String username;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Set<Role> roles;

    @Temporal(TemporalType.DATE)
    private Date registrationDate;

    @OneToMany(mappedBy = "user")
    private List<Budget> budgets;

    @OneToMany(mappedBy = "user")
    private List<Goal> goals;

    @OneToMany(mappedBy = "user")
    private List<Transaction> transactions;

    public User() {}

    public User(String name, String password, Date registrationDate) {
        this.username = name;
        this.password = password;
        this.roles = new HashSet<>();
        this.registrationDate = registrationDate;
    }

    public User(String name, String password, Set<Role> roles, Date registrationDate) {
        this.username = name;
        this.password = password;
        this.roles = roles;
        this.registrationDate = registrationDate;
    }
}

