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
@Service
public class TransactionService {
    private TransactionRepository transactionRepository;
    private BankAccountRepository bankAccountRepository;
    private TransactionMapperService mapper;

    public TransactionService(TransactionRepository transactionRepository, BankAccountRepository bankAccountRepository, TransactionMapperService mapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.mapper = mapper;
        this.transactionRepository = transactionRepository;
    }

    public List<TransactionResponse> getAllTransactions()
    {
        return mapper.getAllTransactions(transactionRepository.findAll());
    }
    public List<TransactionResponse> getAllTransactionsByAccountId(String iban)
    {
       List<Transaction> transactions = transactionRepository.findAllByToOrFromIban(iban);
        return mapper.getAllTransactions(transactions);
    }
    public TransactionResponse getTransactionByid(long id)
    {
       return mapper.toResponse(transactionRepository.findById(id).orElseThrow());
    }
   @Transactional(rollbackFor = Exception.class)
    public TransactionResponse createTransfer(TransferRequest transaction)
    { 
        BankAccount fromAccount = bankAccountRepository.findByIdForUpdate(transaction.getFromAccount()).orElseThrow(() -> new RuntimeException("From bank account not found"));
        BankAccount toAccount = bankAccountRepository.findByIdForUpdate(transaction.getToAccount()).orElseThrow(() -> new RuntimeException("To bank account not found"));
        validateTransfer(transaction, fromAccount,toAccount);

        fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));

        TransferTransaction transferTransaction = mapper.toTransferEntity(transaction);
        transferTransaction.setFromBankAccount(fromAccount);
        transferTransaction.setToBankAccount(toAccount);
        TransferTransaction saved = transactionRepository.save(transferTransaction);
        return mapper.toResponse(saved);
    }  
    private void validateTransfer(TransferRequest transaction, BankAccount fromAccount, BankAccount toAccount) {
        validateSufficientBalance(fromAccount, transaction.getAmount());
        validateWithinSingleTransferLimit(fromAccount, transaction.getAmount());
        validateNotSameAccountTransfer(fromAccount,toAccount);
        validatePositiveAmount(transaction.getAmount());
        validateNotDiffrentUserSavingsTransfer( fromAccount,toAccount);
        
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

    private void validateNotSameAccountTransfer(BankAccount fromAccount, BankAccount toAccount)
    {
        if (fromAccount.getIban().equals(toAccount.getIban())) {
            throw new IllegalArgumentException("transfer to the same account is not allowed");
        }
    }
    private void validatePositiveAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
    }
    private void validateNotDiffrentUserSavingsTransfer(BankAccount fromAccount, BankAccount toAccount)
    {
        if (fromAccount.getUser().getId() != toAccount.getUser().getId() && ( fromAccount.getBankAccountType() == MoneyMachine.models.enums.BankAccountType.SAVINGS || toAccount.getBankAccountType() == MoneyMachine.models.enums.BankAccountType.SAVINGS)) {
            throw new IllegalArgumentException("transfer between different users' savings accounts is not allowed");
        }
    }
    {

    }
       

}
