package MoneyMachine.services;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

import MoneyMachine.exception.InvalidArgumentsException;
import MoneyMachine.exception.NotFoundException;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.User;
import MoneyMachine.models.WithdrawTransaction;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.repositories.TransactionRepository;
import MoneyMachine.services.interfaces.AuthenticationService;
import MoneyMachine.services.interfaces.BankAccountService;
import MoneyMachine.services.interfaces.TransactionService;

@Service
public class TransactionServiceImpl implements TransactionService {

    private BankAccountService bankAccountService;
    private AuthenticationService authenticationService;
    private TransactionRepository transactionRepository;

    public TransactionServiceImpl(BankAccountService bankAccountService, AuthenticationService authenticationService, TransactionRepository transactionRepository) {
        this.bankAccountService = bankAccountService;
        this.authenticationService = authenticationService;
        this.transactionRepository = transactionRepository;
    }

    private void throwIfMoneyAmountIsNotValid(BigDecimal amount) {
    
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InvalidArgumentsException("Amount cannot be less or equal to 0.");
        }
    }

    private void throwIfWithdrawAmountIsNotValid(BigDecimal amount, BankAccount bankAccount) {

        if (bankAccount.getBalance().subtract(amount).compareTo(bankAccount.getAbsoluteLimit()) < 0) {
            throw new InvalidArgumentsException("Total amount cannot be less the absolute limit.");
        }
    }

    @Override
    public DepositTransaction depositAmountIntoBankAccount(String toIban, BigDecimal amount) {
        
        throwIfMoneyAmountIsNotValid(amount);

        BankAccount toBankAccount = bankAccountService.getBankAccountByIban(toIban);

        this.bankAccountService.setBankAccountBalance(toIban, toBankAccount.getBalance().add(amount));

        User loggedInUser = authenticationService.getLoggedInUser();

        DepositTransaction depositTransaction = new DepositTransaction();
        depositTransaction.setInitiatingUser(loggedInUser);
        depositTransaction.setAmount(amount);
        depositTransaction.setMessage(String.format("Deposited € %s into bank account: %s.", amount, toIban));
        depositTransaction.setIsActive(true);
        depositTransaction.setToBankAccount(toBankAccount);

        transactionRepository.save(depositTransaction);

        return depositTransaction;
    }

    @Override
    public WithdrawTransaction withdrawAmountIntoBankAccount(String fromIban, BigDecimal amount) {
        
        throwIfMoneyAmountIsNotValid(amount);

        BankAccount fromBankAccount = bankAccountService.getBankAccountByIban(fromIban);
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

        return withdrawTransaction;
    }
}
