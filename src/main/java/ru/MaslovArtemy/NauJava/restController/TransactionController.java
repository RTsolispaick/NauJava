package ru.MaslovArtemy.NauJava.restController;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
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
    public List<Transaction> getTransactionsByDateAndUser(@RequestParam String date, @RequestParam String nameUser) throws ParseException {
        Optional<User> userOptional = userService.getUserByName(nameUser);

        if (userOptional.isEmpty()) {
            throw new IllegalArgumentException("Пользователь не найден: " + nameUser);
        }

        User user = userOptional.get();

        Date transactionDate;
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        transactionDate = dateFormat.parse(date);

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

    @ExceptionHandler(Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public Exception exceptionServer(Exception e) {
        Exception exception = new Exception(e.getMessage());
        exception.setStackTrace(new StackTraceElement[] {});
        return exception;
    }

    @ExceptionHandler(ParseException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public Exception exceptionParseDate() {
        Exception exception = new Exception("Некорректный формат даты в запросе!");
        exception.setStackTrace(new StackTraceElement[] {});
        return exception;
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public Exception exceptionCategoryNotFound(Exception e) {
        Exception exception = new Exception(e.getMessage());
        exception.setStackTrace(new StackTraceElement[] {});
        return exception;
    }
}
