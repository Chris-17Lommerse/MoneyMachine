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

    public DataSeeder(TransactionRepository transactionRepository, UserRepository userRepository,
            BankAccountRepository bankAccountRepository, AuthenticationService authenticationService) {
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
        this.authenticationService = authenticationService;
    }

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

        userRepository.save(user);

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

        userRepository.save(userWithoutBankAccount);

        User user2 = new User();
        user2.setFirstName("user2LastName");
        user2.setLastName("user2LastName");
        user2.setEmail("user2@user2.user2");
        user2.setBsn("987654321");
        user2.setPhoneNumber("+31 6 87 65 43 21");
        user2.setRole(Role.USER);
        user2.setPassword(authenticationService.getHashedPassword("password"));
        user2.setIsActive(true);
        user2.setIsApproved(true);
        userRepository.save(user2);

        User user3 = new User();
        user3.setFirstName("user3LastName");
        user3.setLastName("user3LastName");
        user3.setEmail("user3@user3.user3");
        user3.setBsn("234567890");
        user3.setPhoneNumber("+31 6 56 45 32 12");
        user3.setRole(Role.USER);
        user3.setPassword(authenticationService.getHashedPassword("password"));
        user3.setIsActive(true);
        user3.setIsApproved(true);
        userRepository.save(user3);

        User user4 = new User();
        user4.setFirstName("user4LastName");
        user4.setLastName("user4LastName");
        user4.setEmail("user4@user4.user4");
        user4.setBsn("14567890");
        user4.setPhoneNumber("+31 6 83 44 69 52");
        user4.setRole(Role.USER);
        user4.setPassword(authenticationService.getHashedPassword("password"));
        user4.setIsActive(true);
        user4.setIsApproved(true);
        userRepository.save(user4);

        BankAccount checkingBankAccount = new BankAccount();
        checkingBankAccount.setIban("NL91ABNA0417164300");
        checkingBankAccount.setUser(user);
        checkingBankAccount.setBalance(new BigDecimal("100"));
        checkingBankAccount.setAbsoluteLimit(new BigDecimal("-100"));
        checkingBankAccount.setSingleTransferLimit(new BigDecimal("100"));
        checkingBankAccount.setDailyTransferLimit(new BigDecimal("100"));
        checkingBankAccount.setBankAccountType(BankAccountType.CHECKING);
        checkingBankAccount.setIsActive(true);

        bankAccountRepository.save(checkingBankAccount);

        BankAccount savingsBankAccount = new BankAccount();
        savingsBankAccount.setIban("NL47INHO0582937105");
        savingsBankAccount.setUser(user);
        savingsBankAccount.setBalance(new BigDecimal("133767"));
        savingsBankAccount.setAbsoluteLimit(new BigDecimal("-1000"));
        savingsBankAccount.setSingleTransferLimit(new BigDecimal("300"));
        savingsBankAccount.setDailyTransferLimit(new BigDecimal("1000"));
        savingsBankAccount.setBankAccountType(BankAccountType.SAVINGS);
        savingsBankAccount.setIsActive(true);

        bankAccountRepository.save(savingsBankAccount);

        DepositTransaction depositTransaction = new DepositTransaction();
        depositTransaction.setInitiatingUser(user);
        depositTransaction.setAmount(new BigDecimal("10"));
        depositTransaction.setMessage("Deposit to ATM");
        depositTransaction.setToBankAccount(checkingBankAccount);

        transactionRepository.save(depositTransaction);

        WithdrawTransaction withdrawTransaction = new WithdrawTransaction();
        withdrawTransaction.setInitiatingUser(user);
        withdrawTransaction.setAmount(new BigDecimal("10"));
        withdrawTransaction.setMessage("Withdraw from ATM");
        withdrawTransaction.setFromBankAccount(checkingBankAccount);

        transactionRepository.save(withdrawTransaction);

        TransferTransaction transferTransaction = new TransferTransaction();
        transferTransaction.setInitiatingUser(user);
        transferTransaction.setAmount(new BigDecimal("10"));
        transferTransaction.setMessage("Hello transfer!");
        transferTransaction.setFromBankAccount(checkingBankAccount);
        transferTransaction.setToBankAccount(savingsBankAccount);

        transactionRepository.save(transferTransaction);
    }
}
