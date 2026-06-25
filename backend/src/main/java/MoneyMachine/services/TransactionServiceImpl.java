package MoneyMachine.services;

import java.math.BigDecimal;
import java.util.Objects;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

import java.util.List;
import java.util.ArrayList;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import MoneyMachine.models.enums.Role;
import MoneyMachine.Specification.SpecificationBuilder;
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
import MoneyMachine.models.dtos.requests.FilterRequest;
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
    private SpecificationBuilder specificationBuilder;

    public TransactionServiceImpl(TransactionRepository transactionRepository, BankAccountRepository bankAccountRepository, TransactionMapperService mapper, BankAccountService bankAccountService, AuthenticationService authenticationService, TransactionMapper transactionMapper, TransactionPolicy transactionPolicy, SpecificationBuilder specificationBuilder) {
        this.mapper = mapper;
        this.bankAccountRepository = bankAccountRepository;
        this.transactionRepository = transactionRepository;
        this.bankAccountService = bankAccountService;
        this.authenticationService = authenticationService;
        this.transactionMapper = transactionMapper;
        this.transactionPolicy = transactionPolicy;
        this.specificationBuilder = specificationBuilder;
    }

    public TransactionOverviewResponse getAllTransactions(Pageable pageable, FilterRequest filter){
        Page<Transaction> page =null;
        Specification<Transaction> spec = specificationBuilder.build(filter);

        page = transactionRepository.findAll(spec,pageable);
        List<Transaction> transferTransactions = page.getContent();
        List<ITransactionResponse> items = mapper.getAllTransactions(transferTransactions);
        TransactionOverviewResponse response = new TransactionOverviewResponse(items,page.getNumber(),page.getSize());
        
        return response;
    }

    

    public TransactionOverviewResponse getTransactionsByIban(String iban,Pageable pageable,FilterRequest filter){
        
        throwIfUserCannotInteractWithBankAccount(authenticationService.getLoggedInUser(), bankAccountService.getBankAccountEntityByIban(iban));
        Specification<Transaction> spec = specificationBuilder.findByIban(iban);
        Page<Transaction> page = transactionRepository.findAll(spec,pageable);
        List<Transaction> transferTransactions = page.getContent();
        TransactionOverviewResponse allFilterdTransactions=this.getAllTransactions(pageable, filter);
        List<Transaction> allFilteredTransactionsByIban =new ArrayList<Transaction>();

        for (ITransactionResponse transactionResponse : allFilterdTransactions.getTransactions()) {
            for(Transaction transaction:transferTransactions)
            {
                if(transactionResponse.getTransactionId()==transaction.getTransactionId())
                {
                    allFilteredTransactionsByIban.add(transaction);

                }

            }
        }
        List<ITransactionResponse> items = mapper.getAllTransactions(allFilteredTransactionsByIban);
        TransactionOverviewResponse response = new TransactionOverviewResponse(items,page.getNumber(),page.getSize());
        
        return response;
    }

    private void throwIfUserCannotInteractWithBankAccount(User user, BankAccount bankAccount) { 
        if (user.getRole() != Role.EMPLOYEE && bankAccount.getUser().getId() != user.getId()) {
            throw new NotAuthorizedException(String.format("You cannot perform actions on bank account: %s.", bankAccount.getIban()));
        }
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

        transactionPolicy.enforceTransactionDepositPolicy(loggedInUser, amount, toBankAccount);
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
    public TransactionOverviewResponse getTransactionsByUserId(Long id, Pageable pageable, FilterRequest filter){

        BankAccountOverviewResponse bankAccountOverviewResponse = bankAccountService.getAllBankAccountsByUserId(id, pageable);
        List<BankAccountResponse> bankAccounts = bankAccountOverviewResponse.getItems();
        TransactionOverviewResponse transactions = new TransactionOverviewResponse(new ArrayList<>(), pageable.getPageNumber(), pageable.getPageSize());

        for(BankAccountResponse bankAccount : bankAccounts){

            String iban = bankAccount.getIban();
            TransactionOverviewResponse overview = getTransactionsByIban(iban, pageable,filter);

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
