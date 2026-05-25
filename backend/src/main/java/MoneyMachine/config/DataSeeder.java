package MoneyMachine.config;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

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
    @Transactional(rollbackFor = Exception.class)
    @Override
    public void run(ApplicationArguments args) throws Exception {
        
        User user = new User();
        user.setFirstName("userFirstName");
        user.setLastName("userLastName");
        user.setEmail("user@user.user");
        user.setBsn("123456789");
        user.setPhoneNumber("+31 6 12 34 56 78");
        user.setRole(Role.USER);
        user.setPassword(authenticationService.getHashedPassword("password"));
        user.setIsActive(true);
        user.setIsApproved(true);

        user = userRepository.save(user);

        User userWithoutBankAccount = new User();
        userWithoutBankAccount.setFirstName("employeeFirstName");
        userWithoutBankAccount.setLastName("employeeLastName");
        userWithoutBankAccount.setEmail("employee@employee.employee");
        userWithoutBankAccount.setBsn("123456749");
        userWithoutBankAccount.setPhoneNumber("+31 6 12 34 54 78");
        userWithoutBankAccount.setRole(Role.EMPLOYEE);
        userWithoutBankAccount.setPassword(authenticationService.getHashedPassword("password"));
        userWithoutBankAccount.setIsActive(false);
        userWithoutBankAccount.setIsApproved(false);

        userWithoutBankAccount = userRepository.save(userWithoutBankAccount);

        BankAccount bankAccount1 = new BankAccount();
        bankAccount1.setIban("NL91ABNA0417164300");
        bankAccount1.setUser(user);
        bankAccount1.setBalance(new BigDecimal("100"));
        bankAccount1.setAbsoluteLimit(new BigDecimal("-100"));
        bankAccount1.setSingleTransferLimit(new BigDecimal("100"));
        bankAccount1.setDailyTransferLimit(new BigDecimal("100"));
        bankAccount1.setBankAccountType(BankAccountType.CHECKING);
        bankAccount1.setIsActive(true);

        bankAccount1 = bankAccountRepository.save(bankAccount1);
        BankAccount bankAccount2 = new BankAccount();
        bankAccount2.setIban("NL91ABNA0417164301");
        bankAccount2.setUser(user);
        bankAccount2.setBalance(new BigDecimal("100"));
        bankAccount2.setAbsoluteLimit(new BigDecimal("-100"));
        bankAccount2.setSingleTransferLimit(new BigDecimal("100"));
        bankAccount2.setDailyTransferLimit(new BigDecimal("100"));
        bankAccount2.setBankAccountType(BankAccountType.CHECKING);
        bankAccount2.setIsActive(true);

        bankAccount2 = bankAccountRepository.save(bankAccount2);

        DepositTransaction depositTransaction = new DepositTransaction();
        depositTransaction.setInitiatingUser(user);
        depositTransaction.setAmount(new BigDecimal("10"));
        depositTransaction.setMessage("Hello deposit!");
        depositTransaction.setActive(true);
        depositTransaction.setToBankAccount(bankAccount1);
        depositTransaction.setDateTime(LocalDateTime.now());

        depositTransaction = transactionRepository.save(depositTransaction);

        WithdrawTransaction withdrawTransaction = new WithdrawTransaction();
        withdrawTransaction.setInitiatingUser(user);
        withdrawTransaction.setAmount(new BigDecimal("10"));
        withdrawTransaction.setMessage("Hello withdraw!");
        withdrawTransaction.setActive(true);
        withdrawTransaction.setFromBankAccount(bankAccount1);
        withdrawTransaction.setDateTime(LocalDateTime.now());

        withdrawTransaction = transactionRepository.save(withdrawTransaction);

        TransferTransaction transferTransaction = new TransferTransaction();
        transferTransaction.setInitiatingUser(user);
        transferTransaction.setAmount(new BigDecimal("10"));
        transferTransaction.setMessage("Hello transfer!");
        transferTransaction.setActive(true);
        transferTransaction.setFromBankAccount(bankAccount1);
        transferTransaction.setToBankAccount(bankAccount2);
        transferTransaction.setDateTime(LocalDateTime.now());

       transferTransaction= transactionRepository.save(transferTransaction);
    }
}
