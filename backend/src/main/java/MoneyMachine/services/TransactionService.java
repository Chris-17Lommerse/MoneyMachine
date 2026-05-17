package MoneyMachine.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import MoneyMachine.models.Transaction;
import MoneyMachine.models.TransferTransaction;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.repositories.TransactionRepository;
import MoneyMachine.repositories.DepositTransactionRepository;
import MoneyMachine.repositories.WithdrawTransactionRepository;
import MoneyMachine.repositories.TransferTransactionRepository;
@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private BankAccountRepository bankAccountRepository;
    private DepositTransactionRepository depositTransactionRepository;
    private WithdrawTransactionRepository withdrawTransactionRepository;
    private TransferTransactionRepository transferTransactionRepository;

    public TransactionService(TransactionRepository transactionRepository, BankAccountRepository bankAccountRepository, DepositTransactionRepository depositTransactionRepository, WithdrawTransactionRepository withdrawTransactionRepository, TransferTransactionRepository transferTransactionRepository) {
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.depositTransactionRepository = depositTransactionRepository;
        this.withdrawTransactionRepository = withdrawTransactionRepository;
        this.transferTransactionRepository = transferTransactionRepository;
    }

    public List<Transaction> getAllTransactions()
    {
       return transactionRepository.findAll();
    }
    public List<Transaction> getAllTransactionsByAccountId(String iban)
    {
        List<Transaction> transactions = new  ArrayList<Transaction>();
        

        transactions.addAll(transferTransactionRepository.findByFromBankAccount_Iban(iban));
        transactions.addAll(transferTransactionRepository.findByToBankAccount_Iban(iban));
        transactions.addAll(depositTransactionRepository.findByToBankAccount_Iban(iban));
        transactions.addAll(withdrawTransactionRepository.findByFromBankAccount_Iban(iban));
        return transactions;
    }
    public Transaction getTransactionByid(long id)
    {
       return transactionRepository.findById(id).orElseThrow();
    }
    @Transactional(rollbackFor = Exception.class)
    public TransferTransaction createTransaction(TransferTransaction transaction)
    {
        bankAccountRepository.pay(transaction.getFromBankAccount().getIban(), transaction.getAmount());
        bankAccountRepository.receive(transaction.getToBankAccount().getIban(), transaction.getAmount());
        return transactionRepository.save(transaction);
    }  
}
