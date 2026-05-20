package MoneyMachine.services;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import MoneyMachine.models.BankAccount;
import MoneyMachine.models.Transaction;
import MoneyMachine.models.TransferTransaction;
import MoneyMachine.models.dtos.responses.TransactionResponse;
import MoneyMachine.models.dtos.requests.TransferRequest;
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
    private TransactionMapperService mapper;

    public TransactionService(TransactionRepository transactionRepository, BankAccountRepository bankAccountRepository, DepositTransactionRepository depositTransactionRepository, WithdrawTransactionRepository withdrawTransactionRepository, TransferTransactionRepository transferTransactionRepository, TransactionMapperService mapper) {
        this.transactionRepository = transactionRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.depositTransactionRepository = depositTransactionRepository;
        this.withdrawTransactionRepository = withdrawTransactionRepository;
        this.transferTransactionRepository = transferTransactionRepository;
        this.mapper = mapper;
    }

    public List<TransactionResponse> getAllTransactions()
    {
        return mapper.getAllTransactions(transactionRepository.findAll());
    }
    public List<TransactionResponse> getAllTransactionsByAccountId(String iban)
    {
       List<Transaction> transactions = new  ArrayList<Transaction>();
        transactions.addAll(transferTransactionRepository.findByFromBankAccount_Iban(iban));
        transactions.addAll(transferTransactionRepository.findByToBankAccount_Iban(iban));
        transactions.addAll(depositTransactionRepository.findByToBankAccount_Iban(iban));
        transactions.addAll(withdrawTransactionRepository.findByFromBankAccount_Iban(iban));
        return mapper.getAllTransactions(transactions);
    }
    public TransactionResponse getTransactionByid(long id)
    {
       return mapper.toResponse(transactionRepository.findById(id).orElseThrow());
    }
   @Transactional(rollbackFor = Exception.class)
    public TransactionResponse createTransfer(TransferRequest transaction)
    {  
      BankAccount fromAccount = bankAccountRepository.findById(transaction.getFromAccount()).orElseThrow(() -> new RuntimeException("From bank account not found"));
        validateTransfer(transaction, fromAccount);
        
        bankAccountRepository.findById(transaction.getToAccount()).orElseThrow(() -> new RuntimeException("To bank account not found"));
        TransferTransaction transferTransaction = mapper.toTransferEntity(transaction);

        bankAccountRepository.pay(transaction.getFromAccount(), transaction.getAmount());
        bankAccountRepository.receive(transaction.getToAccount(), transaction.getAmount());
       TransferTransaction saved = transactionRepository.save(transferTransaction);
       return mapper.toResponse(saved);
    }  
    private void validateTransfer(TransferRequest transaction, BankAccount fromAccount) {
        validateSufficientBalance(fromAccount, transaction.getAmount());
        validateWithinSingleTransferLimit(fromAccount, transaction.getAmount());
        validateNotSelfTransfer(fromAccount, transaction.getToAccount());
        validatePositiveAmount(transaction.getAmount());
        
    }
    private void validateSufficientBalance(BankAccount fromAccount, BigDecimal amount) {
        if (fromAccount.getBalance().subtract(amount).compareTo(fromAccount.getAbsoluteLimit()) < 0) {
            throw new IllegalArgumentException("Insufficient funds");
        }
    }

    private void validateWithinSingleTransferLimit(BankAccount fromAccount, BigDecimal amount) {
        if (fromAccount.getSingleTransferLimit().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Transfer amount exceeds single transfer limit");
        }
    }

    private void validateNotSelfTransfer(BankAccount fromAccount, String toIban)
    {
        if (fromAccount.getIban().equals(toIban)) {
            throw new IllegalArgumentException("Self-transfer is not allowed");
        }
    }
    private void validatePositiveAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
    }

}
