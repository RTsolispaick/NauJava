package ru.MaslovArtemy.NauJava.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Setter @Getter
@Entity
public class Goal {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private Float targetAmount;

    @Temporal(TemporalType.DATE)
    private Date deadline;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Goal() {}

    public Goal(String name, Float targetAmount, Date deadline, User user) {
        this.name = name;
        this.targetAmount = targetAmount;
        this.deadline = deadline;
        this.user = user;
    }
}
