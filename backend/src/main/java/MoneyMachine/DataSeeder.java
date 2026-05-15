package MoneyMachine;

import java.math.BigDecimal;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import MoneyMachine.models.BankAccount;
import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.TransferTransaction;
import MoneyMachine.models.User;
import MoneyMachine.models.WithdrawTransaction;
import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.models.enums.Role;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.repositories.TransactionRepository;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.interfaces.AuthenticationService;

@Component
public class DataSeeder implements ApplicationRunner {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;
    private final AuthenticationService authenticationService;

    public DataSeeder(TransactionRepository transactionRepository, UserRepository userRepository, BankAccountRepository bankAccountRepository, AuthenticationService authenticationService){
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.authenticationService = authenticationService;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        User user = new User("testFirstName", "testLastName", "user@user.user", "123456789", "+31 6 12 34 56 78", Role.USER, authenticationService.getHashedPassword("password"), true, true);
        userRepository.save(user);

        User userWithoutBankAccount = new User("test", "test", "test@test.test", "123456749", "+31 6 12 34 54 78", Role.EMPLOYEE, authenticationService.getHashedPassword("test"), false, false);
        userRepository.save(userWithoutBankAccount);
        BankAccount bankAccount = new BankAccount("NL91ABNA0417164300", user, new BigDecimal("100"), new BigDecimal("-100"), new BigDecimal("100"), new BigDecimal("100"), BankAccountType.CHECKING, true);
        bankAccountRepository.save(bankAccount);

        DepositTransaction depositTransaction = new DepositTransaction(user, new BigDecimal("10"), "Hello deposit!", true, bankAccount);
        transactionRepository.save(depositTransaction);

        WithdrawTransaction withdrawTransaction = new WithdrawTransaction(user, new BigDecimal("10"), "Hello withdraw!", true, bankAccount);
        transactionRepository.save(withdrawTransaction);

        TransferTransaction transferTransaction = new TransferTransaction(user, new BigDecimal("10"), "Hello transfer!", true, bankAccount, bankAccount);
        transactionRepository.save(transferTransaction);
    }
}
