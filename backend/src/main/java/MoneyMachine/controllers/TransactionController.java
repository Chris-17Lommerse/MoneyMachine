package main.java.MoneyMachine.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import MoneyMachine.models.TransferTransaction;
import main.java.MoneyMachine.services.TransactionService;

@RestController
public class TransactionController {
    private TransactionService transactionService;
    
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;

    }

    @GetMapping("/transactions")
    public String getTransactions() {
        return transactionService.getAllTransactions();
    }

    @GetMapping("/transactions/account/{accountId}")
    public String getTransactionsByAccountId(@PathVariable Long accountId) {
        return transactionService.getAllTransactionsByAccountId(accountId);
    }

    @PostMapping("/transactions")
    public String createTransaction(@RequestBody TransferTransaction transaction) {
        return transactionService.createTransaction(transaction);
    }

    @GetMapping("/transactions/{id}")
    public String getTransactionById(@PathVariable Long id) {
        return transactionService.getTransactionById(id);
    }

    @PutMapping("/transactions/{id}")
    public String updateTransaction(@PathVariable Long id, @RequestBody TransferTransaction transaction) {
        return transactionService.updateTransaction(id, transaction);
    }

    @DeleteMapping("/transactions/{id}")
    public String deleteTransaction(@PathVariable Long id) {
        return transactionService.deleteTransaction(id);
    }

}
