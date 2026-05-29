package MoneyMachine.services;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import MoneyMachine.mappers.TransactionMapper;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.Transaction;
import MoneyMachine.models.TransferTransaction;
import MoneyMachine.models.User;
import MoneyMachine.models.WithdrawTransaction;
import MoneyMachine.models.dtos.responses.DepositTransactionResponse;
import MoneyMachine.models.dtos.responses.TransactionResponse;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;
import MoneyMachine.models.dtos.requests.TransferRequest;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.repositories.TransactionRepository;
import MoneyMachine.services.interfaces.AuthenticationService;
import MoneyMachine.services.interfaces.BankAccountService;
import MoneyMachine.services.interfaces.TransactionService;
@Service
public class TransactionServiceImpl implements TransactionService {
    private BankAccountRepository bankAccountRepository;
    private TransactionMapperService mapper;
    private final BankAccountService bankAccountService;
    private final AuthenticationService authenticationService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;


    public TransactionServiceImpl(TransactionRepository transactionRepository, BankAccountRepository bankAccountRepository, TransactionMapperService mapper,BankAccountService bankAccountService, AuthenticationService authenticationService, TransactionMapper transactionMapper) {
        this.bankAccountRepository = bankAccountRepository;
        this.mapper = mapper;
        this.transactionRepository = transactionRepository;
        this.bankAccountService = bankAccountService;
        this.authenticationService = authenticationService;
        this.transactionMapper = transactionMapper;
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

    @Override
    public DepositTransactionResponse depositAmountIntoBankAccount(String toIban, BigDecimal amount) {

        BankAccount toBankAccount = bankAccountService.getBankAccountByIban(toIban);
        throwIfMoneyAmountIsNotValid(amount, toBankAccount);

        this.bankAccountService.setBankAccountBalance(toIban, toBankAccount.getBalance().add(amount));

        User loggedInUser = authenticationService.getLoggedInUser();

        DepositTransaction depositTransaction = new DepositTransaction();
        depositTransaction.setInitiatingUser(loggedInUser);
        depositTransaction.setAmount(amount);
        depositTransaction.setMessage(String.format("Deposited € %s into bank account: %s.", amount, toIban));
        depositTransaction.setIsActive(true);
        depositTransaction.setToBankAccount(toBankAccount);

        transactionRepository.save(depositTransaction);

        return transactionMapper.toDepositTransactionResponse(depositTransaction);
    }

    @Override
    public WithdrawTransactionResponse withdrawAmountIntoBankAccount(String fromIban, BigDecimal amount) {

        BankAccount fromBankAccount = bankAccountService.getBankAccountByIban(fromIban);
        throwIfMoneyAmountIsNotValid(amount, fromBankAccount);
        throwIfWithdrawAmountIsNotValid(amount, fromBankAccount);

        this.bankAccountService.setBankAccountBalance(fromIban, fromBankAccount.getBalance().subtract(amount));

        User loggedInUser = authenticationService.getLoggedInUser();
        
        WithdrawTransaction withdrawTransaction = new WithdrawTransaction();
        withdrawTransaction.setInitiatingUser(loggedInUser);
        withdrawTransaction.setAmount(amount);
        withdrawTransaction.setMessage(String.format("Withdrawn € %s from bank account: %s.", amount, fromIban));
        withdrawTransaction.setIsActive(true);
        withdrawTransaction.setFromBankAccount(fromBankAccount);

        transactionRepository.save(withdrawTransaction);

        return transactionMapper.toWithdrawTransactionResponse(withdrawTransaction);
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
    
    private void throwIfMoneyAmountIsNotValid(BigDecimal amount, BankAccount bankAccount) {
    
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount cannot be less or equal to 0.");
        }

        if (amount.compareTo(bankAccount.getSingleTransferLimit()) > 0) {
            throw new IllegalArgumentException(String.format("Amount cannot be more than the single transfer limit %s.", bankAccount.getSingleTransferLimit()));
        }
    }

    private void throwIfWithdrawAmountIsNotValid(BigDecimal amount, BankAccount bankAccount) {

        if (bankAccount.getBalance().subtract(amount).compareTo(bankAccount.getAbsoluteLimit()) < 0) {
            throw new IllegalArgumentException("Total amount cannot be less the absolute limit.");
        }
    }

   
       

}
