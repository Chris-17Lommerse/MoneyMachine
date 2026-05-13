package main.java.MoneyMachine.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import MoneyMachine.models.Transaction;
import MoneyMachine.repositories.TransactionRepository;
@Service
public class TransactionService {
    private TransactionRepository transactionRepository;

    public TransactionService(TransactionRepository transactionRepository) {
        this.transactionRepository = transactionRepository;
    }

    public List<Transaction> getAllTransactions()
    {
       return transactionRepository.findAll();
    }
    public List<Transaction> getAllTransactionsByAccountId(int accountId)
    {
        List<Transaction> transactions = new  ArrayList<Transaction>();
        List<Transaction> fromTransactions = new  ArrayList<Transaction>();
        List<Transaction> toTransactions = new  ArrayList<Transaction>();

        fromTransactions.addAll(transactionRepository.getTransactionByFromAccountId(accountId));
        toTransactions.addAll(transactionRepository.getTransactionByToAccountId(accountId));

        transactions.addAll(fromTransactions);
        transactions.addAll(toTransactions);
        return transactions;
    }
    public Transaction getTransactionById(int transactionId)
    {
       return transactionRepository.findById(transactionId);
    }
    public Transaction createTransaction(Transaction transaction)
    {
        return transactionRepository.save(transaction);
    }
    public Transaction updateTransaction(int transactionId, Transaction transaction)
    {
        return transactionRepository.save(transactionId,transaction);
    }
    public void deleteTransaction(int transactionId)
    {
        transactionRepository.deleteById(transactionId);
    }

    
}
