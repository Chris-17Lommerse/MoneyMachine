package MoneyMachine.services;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import java.util.List;
import java.util.ArrayList;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import MoneyMachine.models.enums.Role;
import MoneyMachine.exception.NotAuthorizedException;
import MoneyMachine.policies.TransactionPolicy;
import MoneyMachine.mappers.TransactionMapper;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.Transaction;
import MoneyMachine.models.TransferTransaction;
import MoneyMachine.models.User;
import MoneyMachine.models.WithdrawTransaction;
import MoneyMachine.models.dtos.responses.DepositTransactionResponse;
import MoneyMachine.models.dtos.responses.TransactionOverviewResponse;
import MoneyMachine.models.dtos.responses.ITransactionResponse;
import MoneyMachine.models.dtos.responses.TransferTransactionResponse;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.repositories.TransactionRepository;
import MoneyMachine.services.interfaces.AuthenticationService;
import MoneyMachine.services.interfaces.BankAccountService;
import MoneyMachine.services.interfaces.TransactionService;

import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import MoneyMachine.models.dtos.responses.BankAccountResponse;

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

    public TransactionOverviewResponse getAllTransactions(Pageable pageable)
    {
        Page<Transaction> page = transactionRepository.findAll(pageable);
        List<Transaction> transferTransactions = page.getContent();
        List<ITransactionResponse> items = mapper.getAllTransactions(transferTransactions);
        TransactionOverviewResponse response = new TransactionOverviewResponse(items,page.getNumber(),page.getSize());
        return response;
    }
    public TransactionOverviewResponse getTransactionsByIban(String iban,Pageable pageable)
    {
        throwIfUserCannotInteractWithBankAccount(authenticationService.getLoggedInUser(), bankAccountService.getBankAccountEntityByIban(iban));
        Page<Transaction> page = transactionRepository.findAllByToOrFromIban(iban,pageable);
        List<Transaction> transferTransactions = page.getContent();
        List<ITransactionResponse> items = mapper.getAllTransactions(transferTransactions);
        TransactionOverviewResponse response = new TransactionOverviewResponse(items,page.getNumber(),page.getSize());
        return response;
    }

    private void throwIfUserCannotInteractWithBankAccount(User user, BankAccount bankAccount) {
        
        if (user.getRole() != Role.EMPLOYEE && bankAccount.getUser().getId() != user.getId()) {
            throw new NotAuthorizedException(String.format("You cannot perform actions on bank account: %s.", bankAccount.getIban()));
        }
    }

    public ITransactionResponse getTransactionByid(long id)
    {
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

    @Override
    public TransactionOverviewResponse getTransactionsByUserId(Long id, Pageable pageable){

        BankAccountOverviewResponse bankAccountOverviewResponse = bankAccountService.getAllBankAccountsByUserId(id, pageable);
        List<BankAccountResponse> bankAccounts = bankAccountOverviewResponse.getItems();
        TransactionOverviewResponse transactions = new TransactionOverviewResponse(new ArrayList<>(), pageable.getPageNumber(), pageable.getPageSize());

        for(BankAccountResponse bankAccount : bankAccounts){

            String iban = bankAccount.getIban();
            TransactionOverviewResponse overview = getTransactionsByIban(iban, pageable);

            for (ITransactionResponse transactionResponse : overview.getTransactions()) {

                boolean isNewTransaction = true;

                for (ITransactionResponse existing : transactions.getTransactions()) {

                    if (Objects.equals(existing.getTransactionId(),transactionResponse.getTransactionId())) {

                        isNewTransaction = false;
                        break;
                    }
                }

                if (isNewTransaction) {
                    transactions.getTransactions().add(transactionResponse);
                }
            }
        }
        
        return transactions;
    }
}
