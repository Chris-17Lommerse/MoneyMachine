package MoneyMachine.services;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import MoneyMachine.mappers.TransactionMapper;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.User;
import MoneyMachine.models.WithdrawTransaction;
import MoneyMachine.models.dtos.responses.DepositTransactionResponse;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;
import MoneyMachine.repositories.TransactionRepository;
import MoneyMachine.services.interfaces.AuthenticationService;
import MoneyMachine.services.interfaces.BankAccountService;
import MoneyMachine.services.interfaces.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    private final BankAccountService bankAccountService;
    private final AuthenticationService authenticationService;
    private final TransactionRepository transactionRepository;
    private final TransactionMapper transactionMapper;

    public TransactionServiceImpl(BankAccountService bankAccountService, AuthenticationService authenticationService, TransactionRepository transactionRepository, TransactionMapper transactionMapper) {
        this.bankAccountService = bankAccountService;
        this.authenticationService = authenticationService;
        this.transactionRepository = transactionRepository;
        this.transactionMapper = transactionMapper;
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

    @Override
    public DepositTransactionResponse depositAmountIntoBankAccount(String toIban, BigDecimal amount) {

        BankAccount toBankAccount = bankAccountService.findBankAccountEntityByIban(toIban);
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

        BankAccount fromBankAccount = bankAccountService.findBankAccountEntityByIban(fromIban);
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
}
