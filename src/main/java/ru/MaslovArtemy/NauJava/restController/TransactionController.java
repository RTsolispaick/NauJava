package ru.MaslovArtemy.NauJava.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.MaslovArtemy.NauJava.model.Category;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.model.User;
import ru.MaslovArtemy.NauJava.service.CategoryService;
import ru.MaslovArtemy.NauJava.service.TransactionService;
import ru.MaslovArtemy.NauJava.service.UserService;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("custom/transaction")
public class TransactionController {
    private final TransactionService transactionService;
    private final UserService userService;
    private final CategoryService categoryService;

    @Autowired
    public TransactionController(TransactionService transactionService,
                                 UserService userService,
                                 CategoryService categoryService) {
        this.transactionService = transactionService;
        this.userService = userService;
        this.categoryService = categoryService;
    }

    @GetMapping("/getByDateAndUser")
    public List<Transaction> getTransactionsByDateAndUser(@RequestParam String date, @RequestParam String nameUser) {
        Optional<User> userOptional = userService.getUserByName(nameUser);

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Пользователь не найден: " + nameUser);
        }

        User user = userOptional.get();

        Date transactionDate;
        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            transactionDate = dateFormat.parse(date);
        } catch (ParseException e) {
            throw new IllegalArgumentException("Некорректный формат даты: " + date);
        }

        return transactionService.getTransactionsByDateAndUser(transactionDate, user);
    }

    @GetMapping("/getByCategory")
    public List<Transaction> getTransactionsByCategory(@RequestParam String categoryName) {
        Optional<Category> categoryOptional = categoryService.getCategoryByName(categoryName);

        if (categoryOptional.isEmpty()) {
            throw new IllegalArgumentException("Категория не найдена: " + categoryName);
        }

        Category category = categoryOptional.get();

        return transactionService.getTransactionsByCategory(category);
    }
}
