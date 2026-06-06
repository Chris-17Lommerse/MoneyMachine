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
import MoneyMachine.models.dtos.responses.TransferTransactionResponse;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.repositories.TransactionRepository;
import MoneyMachine.services.interfaces.AuthenticationService;
import MoneyMachine.services.interfaces.BankAccountService;
import MoneyMachine.services.interfaces.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {
    
    private final TransactionPolicy transactionPolicy;
    private final TransactionMapperService mapper;
    private final BankAccountService bankAccountService;
    private final AuthenticationService authenticationService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;
    private final BankAccountRepository bankAccountRepository;

    public TransactionServiceImpl(TransactionRepository transactionRepository, BankAccountRepository bankAccountRepository, TransactionMapperService mapper, BankAccountService bankAccountService, AuthenticationService authenticationService, TransactionMapper transactionMapper, TransactionPolicy transactionPolicy) {
        this.mapper = mapper;
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
        this.bankAccountService = bankAccountService;
        this.authenticationService = authenticationService;
        this.transactionMapper = transactionMapper;
        this.transactionPolicy = transactionPolicy;
    }

    public List<TransferTransactionResponse> getAllTransactions() {
        return mapper.getAllTransactions(transactionRepository.findAll());
    }

    public List<TransferTransactionResponse> getAllTransactionsByAccountId(String iban) {
        List<Transaction> transactions = transactionRepository.findAllByToOrFromIban(iban);
        return mapper.getAllTransactions(transactions);
    }

    public TransferTransactionResponse getTransactionByid(long id) {
       return mapper.toResponse(transactionRepository.findById(id).orElseThrow());
    }

    private void fillCommonTransactionProperties(Transaction transaction, User initiatingUser, BigDecimal amount, String message) {
        
        transaction.setInitiatingUser(initiatingUser);
        transaction.setAmount(amount);
        transaction.setMessage(message);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public TransferTransactionResponse transferAmountBetweenBankAccounts(String fromIban, String toIban, BigDecimal amount, String message) { 
        
        User loggedInUser = authenticationService.getLoggedInUser();
        BankAccount fromBankAccount = bankAccountService.getBankAccountEntityByIban(fromIban);
        BankAccount toBankAccount = bankAccountService.getBankAccountEntityByIban(toIban);
        
        transactionPolicy.enforceTransactionTransferPolicy(loggedInUser, amount, fromBankAccount, toBankAccount);
        bankAccountRepository.decrementBalanceByIban(fromIban, amount);
        bankAccountRepository.incrementBalanceByIban(toIban, amount);

        TransferTransaction transferTransaction = new TransferTransaction();
        fillCommonTransactionProperties(transferTransaction, loggedInUser, amount, message);
        transferTransaction.setFromBankAccount(fromBankAccount);
        transferTransaction.setToBankAccount(toBankAccount);

        transactionRepository.save(transferTransaction);

        return transactionMapper.toTransferTransactionResponse(transferTransaction);
    }  

    @Override
    @Transactional(rollbackFor = Exception.class)
    public DepositTransactionResponse depositAmountIntoBankAccount(String toIban, BigDecimal amount) {
        
        User loggedInUser = authenticationService.getLoggedInUser();
        BankAccount toBankAccount = bankAccountService.getBankAccountEntityByIban(toIban);

        transactionPolicy.enforceTransactionPolicy(loggedInUser, amount, toBankAccount);
        bankAccountRepository.incrementBalanceByIban(toIban, amount);

        DepositTransaction depositTransaction = new DepositTransaction();
        fillCommonTransactionProperties(depositTransaction, loggedInUser, amount, "ATM deposit");
        depositTransaction.setToBankAccount(toBankAccount);

        transactionRepository.save(depositTransaction);

        return transactionMapper.toDepositTransactionResponse(depositTransaction);
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public WithdrawTransactionResponse withdrawAmountIntoBankAccount(String fromIban, BigDecimal amount) {

        User loggedInUser = authenticationService.getLoggedInUser();
        BankAccount fromBankAccount = bankAccountService.getBankAccountEntityByIban(fromIban);
        
        transactionPolicy.enforceTransactionWithdrawPolicy(loggedInUser, amount, fromBankAccount);
        bankAccountRepository.decrementBalanceByIban(fromIban, amount);
        
        WithdrawTransaction withdrawTransaction = new WithdrawTransaction();
        fillCommonTransactionProperties(withdrawTransaction, loggedInUser, amount, "ATM withdraw");
        withdrawTransaction.setFromBankAccount(fromBankAccount);

        transactionRepository.save(withdrawTransaction);

        return transactionMapper.toWithdrawTransactionResponse(withdrawTransaction);
    }
}
