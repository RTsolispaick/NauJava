package ru.MaslovArtemy.NauJava.model.DTO;

import java.util.Date;

public record TransactionDTO(
        Long id,
        Double amount,
        Date date,
        String description,
        String type,
        Long userId,
        Long budgetId,
        Long categoryId
) {
}
