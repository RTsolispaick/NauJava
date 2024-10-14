package ru.MaslovArtemy.NauJava.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.MaslovArtemy.NauJava.model.Transaction;
import ru.MaslovArtemy.NauJava.service.TransactionService;

@Controller
@RequestMapping("custom/transaction/view")
public class TransactionControllerView {
    private final TransactionService transactionService;

    @Autowired
    public TransactionControllerView(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/list")
    public String transactionListView(Model model) {
        Iterable<Transaction> transactions = transactionService.getAllTransactions();
        model.addAttribute("transactions", transactions);
        return "transactionList";
    }
}
