package MoneyMachine.config;

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

        User user = new User();
        user.setFirstName("testFirstName");
        user.setLastName("testLastName");
        user.setEmail("user@user.user");
        user.setBsn("123456789");
        user.setPhoneNumber("+31 6 12 34 56 78");
        user.setRole(Role.ROLE_USER);
        user.setPassword(authenticationService.getHashedPassword("password"));
        user.setIsActive(true);
        user.setIsApproved(true);

        userRepository.save(user);

        User userWithoutBankAccount = new User();
        userWithoutBankAccount.setFirstName("test");
        userWithoutBankAccount.setLastName("test");
        userWithoutBankAccount.setEmail("test@test.test");
        userWithoutBankAccount.setBsn("123456749");
        userWithoutBankAccount.setPhoneNumber("+31 6 12 34 54 78");
        userWithoutBankAccount.setRole(Role.ROLE_EMPLOYEE);
        userWithoutBankAccount.setPassword(authenticationService.getHashedPassword("test"));
        userWithoutBankAccount.setIsActive(false);
        userWithoutBankAccount.setIsApproved(false);

        userRepository.save(userWithoutBankAccount);

        BankAccount bankAccount = new BankAccount();
        bankAccount.setIban("NL91ABNA0417164300");
        bankAccount.setUser(user);
        bankAccount.setBalance(new BigDecimal("100"));
        bankAccount.setAbsoluteLimit(new BigDecimal("-100"));
        bankAccount.setSingleTransferLimit(new BigDecimal("100"));
        bankAccount.setDailyTransferLimit(new BigDecimal("100"));
        bankAccount.setBankAccountType(BankAccountType.CHECKING);
        bankAccount.setIsActive(true);

        bankAccountRepository.save(bankAccount);

        DepositTransaction depositTransaction = new DepositTransaction();
        depositTransaction.setInitiatingUser(user);
        depositTransaction.setAmount(new BigDecimal("10"));
        depositTransaction.setMessage("Hello deposit!");
        depositTransaction.setIsActive(true);
        depositTransaction.setToBankAccount(bankAccount);

        transactionRepository.save(depositTransaction);

        WithdrawTransaction withdrawTransaction = new WithdrawTransaction();
        withdrawTransaction.setInitiatingUser(user);
        withdrawTransaction.setAmount(new BigDecimal("10"));
        withdrawTransaction.setMessage("Hello withdraw!");
        withdrawTransaction.setIsActive(true);
        withdrawTransaction.setFromBankAccount(bankAccount);

        transactionRepository.save(withdrawTransaction);

        TransferTransaction transferTransaction = new TransferTransaction();
        transferTransaction.setInitiatingUser(user);
        transferTransaction.setAmount(new BigDecimal("10"));
        transferTransaction.setMessage("Hello transfer!");
        transferTransaction.setIsActive(true);
        transferTransaction.setFromBankAccount(bankAccount);
        transferTransaction.setToBankAccount(bankAccount);

        transactionRepository.save(transferTransaction);
    }
}
