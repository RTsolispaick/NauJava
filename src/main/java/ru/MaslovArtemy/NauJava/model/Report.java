package ru.MaslovArtemy.NauJava.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;

@Setter @Getter
@Entity
public class Report {
    @jakarta.persistence.Id
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private ReportStatus status;

    @Lob
    private String content;

    public Report() {
        this.status = ReportStatus.CREATED;
    }
}
