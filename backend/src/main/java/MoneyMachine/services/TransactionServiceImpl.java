package MoneyMachine.services;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.stereotype.Service;

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

    @Override
    public DepositTransaction depositAmountIntoBankAccount(String toIban, BigDecimal amount) {
        
        //POLICY CHECKS
        BankAccount toBankAccount = bankAccountService.getBankAccountByIban(toIban);
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
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'withdrawAmountIntoBankAccount'");
    }
}
