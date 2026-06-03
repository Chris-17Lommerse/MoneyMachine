package MoneyMachine.services;

import java.math.BigDecimal;
import java.util.List;

import MoneyMachine.policies.TransactionPolicy;

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
    
    private final TransactionPolicy transactionPolicy;
    private BankAccountRepository bankAccountRepository;
    private TransactionMapperService mapper;
    private final BankAccountService bankAccountService;
    private final AuthenticationService authenticationService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(TransactionRepository transactionRepository, BankAccountRepository bankAccountRepository, TransactionMapperService mapper, BankAccountService bankAccountService, AuthenticationService authenticationService, TransactionMapper transactionMapper, TransactionPolicy transactionPolicy) {
        this.bankAccountRepository = bankAccountRepository;
        this.mapper = mapper;
        this.transactionRepository = transactionRepository;
        this.bankAccountService = bankAccountService;
        this.authenticationService = authenticationService;
        this.transactionMapper = transactionMapper;
        this.transactionPolicy = transactionPolicy;
    }

    public List<TransactionResponse> getAllTransactions() {
        return mapper.getAllTransactions(transactionRepository.findAll());
    }

    public List<TransactionResponse> getAllTransactionsByAccountId(String iban) {
        List<Transaction> transactions = transactionRepository.findAllByToOrFromIban(iban);
        return mapper.getAllTransactions(transactions);
    }

    public TransactionResponse getTransactionByid(long id) {
       return mapper.toResponse(transactionRepository.findById(id).orElseThrow());
    }

    @Transactional(rollbackFor = Exception.class)
    public TransactionResponse createTransferAsUser(TransferRequest transaction, User user) { 
        
        BankAccount fromAccount = bankAccountRepository.findByIdForUpdate(transaction.getFromAccount()).orElseThrow(() -> new RuntimeException("From bank account not found"));
        BankAccount toAccount = bankAccountRepository.findByIdForUpdate(transaction.getToAccount()).orElseThrow(() -> new RuntimeException("To bank account not found"));
        
        transactionPolicy.enforceTransactionPolicy(user, transaction.getAmount(), fromAccount, toAccount);

        fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));

        TransferTransaction transferTransaction = mapper.toTransferEntity(transaction,user);
        transferTransaction.setFromBankAccount(fromAccount);
        transferTransaction.setToBankAccount(toAccount);
        TransferTransaction saved = transactionRepository.save(transferTransaction);
        
        return mapper.toResponse(saved);
    }  

    @Transactional(rollbackFor = Exception.class)
    public TransactionResponse createTransferAsEmployee(TransferRequest transaction, User user) { 

        BankAccount fromAccount = bankAccountRepository.findByIdForUpdate(transaction.getFromAccount()).orElseThrow(() -> new RuntimeException("From bank account not found"));
        BankAccount toAccount = bankAccountRepository.findByIdForUpdate(transaction.getToAccount()).orElseThrow(() -> new RuntimeException("To bank account not found"));
        
        transactionPolicy.enforceTransactionPolicy(user, transaction.getAmount(), fromAccount, toAccount);

        fromAccount.setBalance(fromAccount.getBalance().subtract(transaction.getAmount()));
        toAccount.setBalance(toAccount.getBalance().add(transaction.getAmount()));

        TransferTransaction transferTransaction = mapper.toTransferEntity(transaction,user);
        transferTransaction.setFromBankAccount(fromAccount);
        transferTransaction.setToBankAccount(toAccount);
        TransferTransaction saved = transactionRepository.save(transferTransaction);

        return mapper.toResponse(saved);
    }  

    @Override
    public DepositTransactionResponse depositAmountIntoBankAccount(String toIban, BigDecimal amount, String message) {

        BankAccount toBankAccount = bankAccountService.getBankAccountEntityByIban(toIban);
        User loggedInUser = authenticationService.getLoggedInUser();

        transactionPolicy.enforceTransactionPolicy(loggedInUser, amount, null, toBankAccount);
        bankAccountService.setBankAccountBalance(toIban, toBankAccount.getBalance().add(amount));

        DepositTransaction depositTransaction = new DepositTransaction();
        depositTransaction.setInitiatingUser(loggedInUser);
        depositTransaction.setAmount(amount);
        depositTransaction.setMessage(message);
        depositTransaction.setToBankAccount(toBankAccount);

        transactionRepository.save(depositTransaction);

        return transactionMapper.toDepositTransactionResponse(depositTransaction);
    }

    @Override
    public WithdrawTransactionResponse withdrawAmountIntoBankAccount(String fromIban, BigDecimal amount, String message) {

        BankAccount fromBankAccount = bankAccountService.getBankAccountEntityByIban(fromIban);
        User loggedInUser = authenticationService.getLoggedInUser();
        
        transactionPolicy.enforceTransactionPolicy(loggedInUser, amount, fromBankAccount, null);
        bankAccountService.setBankAccountBalance(fromIban, fromBankAccount.getBalance().subtract(amount));
        
        WithdrawTransaction withdrawTransaction = new WithdrawTransaction();
        withdrawTransaction.setInitiatingUser(loggedInUser);
        withdrawTransaction.setAmount(amount);
        withdrawTransaction.setMessage(message);
        withdrawTransaction.setFromBankAccount(fromBankAccount);

        transactionRepository.save(withdrawTransaction);

        return transactionMapper.toWithdrawTransactionResponse(withdrawTransaction);
    }
}
