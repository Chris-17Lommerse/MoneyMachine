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

        User employee = new User();
        employee.setFirstName("employeeFirstName");
        employee.setLastName("employeeLastName");
        employee.setEmail("employee@employee.employee");
        employee.setBsn("123456749");
        employee.setPhoneNumber("+31 6 12 34 54 78");
        employee.setRole(Role.EMPLOYEE);
        employee.setPassword(authenticationService.getHashedPassword("password"));
        employee.setIsActive(true);
        employee.setIsApproved(true);

        userRepository.save(employee);

        User user2 = new User();
        user2.setFirstName("Alice");
        user2.setLastName("Johnson");
        user2.setEmail("alice@johnson.com");
        user2.setBsn("987654321");
        user2.setPhoneNumber("+31 6 87 65 43 21");
        user2.setRole(Role.USER);
        user2.setPassword(authenticationService.getHashedPassword("password"));
        user2.setIsActive(true);
        user2.setIsApproved(true);

        userRepository.save(user2);

        User user3 = new User();
        user3.setFirstName("Bob");
        user3.setLastName("Smith");
        user3.setEmail("bob@smith.com");
        user3.setBsn("234567890");
        user3.setPhoneNumber("+31 6 56 45 32 12");
        user3.setRole(Role.USER);
        user3.setPassword(authenticationService.getHashedPassword("password"));
        user3.setIsActive(true);
        user3.setIsApproved(true);

        userRepository.save(user3);

        User user4 = new User();
        user4.setFirstName("Clara");
        user4.setLastName("Williams");
        user4.setEmail("clara@williams.com");
        user4.setBsn("14567890");
        user4.setPhoneNumber("+31 6 83 44 69 52");
        user4.setRole(Role.USER);
        user4.setPassword(authenticationService.getHashedPassword("password"));
        user4.setIsActive(true);
        user4.setIsApproved(true);

        userRepository.save(user4);

        User user5 = new User();
        user5.setFirstName("David");
        user5.setLastName("Brown");
        user5.setEmail("david@brown.com");
        user5.setBsn("345678901");
        user5.setPhoneNumber("+31 6 91 23 45 67");
        user5.setRole(Role.USER);
        user5.setPassword(authenticationService.getHashedPassword("password"));
        user5.setIsActive(true);
        user5.setIsApproved(false);

        userRepository.save(user5);

        User user6 = new User();
        user6.setFirstName("Eva");
        user6.setLastName("Martinez");
        user6.setEmail("eva@martinez.com");
        user6.setBsn("456789012");
        user6.setPhoneNumber("+31 6 34 56 78 90");
        user6.setRole(Role.USER);
        user6.setPassword(authenticationService.getHashedPassword("password"));
        user6.setIsActive(false);
        user6.setIsApproved(false);

        userRepository.save(user6);
        User user7 = new User();
        user7.setFirstName("will");
        user7.setLastName("quinlan");
        user7.setEmail("will@quinlan.com");
        user7.setBsn("456789012");
        user7.setPhoneNumber("+31 6 34 56 78 90");
        user7.setRole(Role.USER);
        user7.setPassword(authenticationService.getHashedPassword("password"));
        user7.setIsActive(true);
        user7.setIsApproved(true);

        userRepository.save(user7);

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

        BankAccount user2CheckingAccount = new BankAccount();
        user2CheckingAccount.setIban("NL93ABNA0004900781");
        user2CheckingAccount.setUser(user2);
        user2CheckingAccount.setBalance(new BigDecimal("2500.00"));
        user2CheckingAccount.setAbsoluteLimit(new BigDecimal("-500"));
        user2CheckingAccount.setSingleTransferLimit(new BigDecimal("500"));
        user2CheckingAccount.setDailyTransferLimit(new BigDecimal("2000"));
        user2CheckingAccount.setBankAccountType(BankAccountType.CHECKING);
        user2CheckingAccount.setIsActive(true);

        bankAccountRepository.save(user2CheckingAccount);

        BankAccount user2SavingsAccount = new BankAccount();
        user2SavingsAccount.setIban("NL28ABNA0473817609");
        user2SavingsAccount.setUser(user2);
        user2SavingsAccount.setBalance(new BigDecimal("15000.00"));
        user2SavingsAccount.setAbsoluteLimit(new BigDecimal("0"));
        user2SavingsAccount.setSingleTransferLimit(new BigDecimal("1000"));
        user2SavingsAccount.setDailyTransferLimit(new BigDecimal("3000"));
        user2SavingsAccount.setBankAccountType(BankAccountType.SAVINGS);
        user2SavingsAccount.setIsActive(true);

        bankAccountRepository.save(user2SavingsAccount);

        BankAccount user3CheckingAccount = new BankAccount();
        user3CheckingAccount.setIban("NL47ABNA0428395174");
        user3CheckingAccount.setUser(user3);
        user3CheckingAccount.setBalance(new BigDecimal("750.00"));
        user3CheckingAccount.setAbsoluteLimit(new BigDecimal("-200"));
        user3CheckingAccount.setSingleTransferLimit(new BigDecimal("250"));
        user3CheckingAccount.setDailyTransferLimit(new BigDecimal("1000"));
        user3CheckingAccount.setBankAccountType(BankAccountType.CHECKING);
        user3CheckingAccount.setIsActive(true);

        bankAccountRepository.save(user3CheckingAccount);

        BankAccount user4CheckingAccount = new BankAccount();
        user4CheckingAccount.setIban("NL62ABNA0123456789");
        user4CheckingAccount.setUser(user4);
        user4CheckingAccount.setBalance(new BigDecimal("300.00"));
        user4CheckingAccount.setAbsoluteLimit(new BigDecimal("0"));
        user4CheckingAccount.setSingleTransferLimit(new BigDecimal("150"));
        user4CheckingAccount.setDailyTransferLimit(new BigDecimal("500"));
        user4CheckingAccount.setBankAccountType(BankAccountType.CHECKING);
        user4CheckingAccount.setIsActive(true);

        bankAccountRepository.save(user4CheckingAccount);

        BankAccount user7CheckingAccount = new BankAccount();
        user7CheckingAccount.setIban("NL62INHO0123456789");
        user7CheckingAccount.setUser(user7);
        user7CheckingAccount.setBalance(new BigDecimal("300.00"));
        user7CheckingAccount.setAbsoluteLimit(new BigDecimal("0"));
        user7CheckingAccount.setSingleTransferLimit(new BigDecimal("100"));
        user7CheckingAccount.setDailyTransferLimit(new BigDecimal("80"));
        user7CheckingAccount.setBankAccountType(BankAccountType.CHECKING);
        user7CheckingAccount.setIsActive(true);

        bankAccountRepository.save(user7CheckingAccount);

        BankAccount employeeCheckingAccount = new BankAccount();
        employeeCheckingAccount.setIban("NL55ABNA0987654321");
        employeeCheckingAccount.setUser(employee);
        employeeCheckingAccount.setBalance(new BigDecimal("9999.00"));
        employeeCheckingAccount.setAbsoluteLimit(new BigDecimal("-1000"));
        employeeCheckingAccount.setSingleTransferLimit(new BigDecimal("5000"));
        employeeCheckingAccount.setDailyTransferLimit(new BigDecimal("10000"));
        employeeCheckingAccount.setBankAccountType(BankAccountType.CHECKING);
        employeeCheckingAccount.setIsActive(true);

        bankAccountRepository.save(employeeCheckingAccount);

        DepositTransaction depositTransaction = new DepositTransaction();
        depositTransaction.setInitiatingUser(user);
        depositTransaction.setAmount(new BigDecimal("10"));
        depositTransaction.setMessage("Deposit to ATM");
        depositTransaction.setToBankAccount(checkingBankAccount);

        transactionRepository.save(depositTransaction);

        DepositTransaction depositTransaction2 = new DepositTransaction();
        depositTransaction2.setInitiatingUser(user2);
        depositTransaction2.setAmount(new BigDecimal("500.00"));
        depositTransaction2.setMessage("Monthly savings deposit");
        depositTransaction2.setToBankAccount(user2CheckingAccount);

        transactionRepository.save(depositTransaction2);

        DepositTransaction depositTransaction3 = new DepositTransaction();
        depositTransaction3.setInitiatingUser(user3);
        depositTransaction3.setAmount(new BigDecimal("50.00"));
        depositTransaction3.setMessage("ATM deposit");
        depositTransaction3.setToBankAccount(user3CheckingAccount);

        transactionRepository.save(depositTransaction3);

        WithdrawTransaction withdrawTransaction = new WithdrawTransaction();
        withdrawTransaction.setInitiatingUser(user);
        withdrawTransaction.setAmount(new BigDecimal("10"));
        withdrawTransaction.setMessage("Withdraw from ATM");
        withdrawTransaction.setFromBankAccount(checkingBankAccount);

        transactionRepository.save(withdrawTransaction);

        WithdrawTransaction withdrawTransaction2 = new WithdrawTransaction();
        withdrawTransaction2.setInitiatingUser(user2);
        withdrawTransaction2.setAmount(new BigDecimal("200.00"));
        withdrawTransaction2.setMessage("ATM withdrawal");
        withdrawTransaction2.setFromBankAccount(user2CheckingAccount);

        transactionRepository.save(withdrawTransaction2);

        WithdrawTransaction withdrawTransaction3 = new WithdrawTransaction();
        withdrawTransaction3.setInitiatingUser(user4);
        withdrawTransaction3.setAmount(new BigDecimal("50.00"));
        withdrawTransaction3.setMessage("Grocery shopping");
        withdrawTransaction3.setFromBankAccount(user4CheckingAccount);

        transactionRepository.save(withdrawTransaction3);

        TransferTransaction transferTransaction = new TransferTransaction();
        transferTransaction.setInitiatingUser(user);
        transferTransaction.setAmount(new BigDecimal("10"));
        transferTransaction.setMessage("Hello transfer!");
        transferTransaction.setFromBankAccount(checkingBankAccount);
        transferTransaction.setToBankAccount(savingsBankAccount);

        transactionRepository.save(transferTransaction);

        TransferTransaction transferTransaction2 = new TransferTransaction();
        transferTransaction2.setInitiatingUser(user2);
        transferTransaction2.setAmount(new BigDecimal("100.00"));
        transferTransaction2.setMessage("Rent payment");
        transferTransaction2.setFromBankAccount(user2CheckingAccount);
        transferTransaction2.setToBankAccount(user3CheckingAccount);

        transactionRepository.save(transferTransaction2);

        TransferTransaction transferTransaction3 = new TransferTransaction();
        transferTransaction3.setInitiatingUser(user3);
        transferTransaction3.setAmount(new BigDecimal("75.00"));
        transferTransaction3.setMessage("Dinner split");
        transferTransaction3.setFromBankAccount(user3CheckingAccount);
        transferTransaction3.setToBankAccount(user4CheckingAccount);

        transactionRepository.save(transferTransaction3);

        TransferTransaction transferTransaction4 = new TransferTransaction();
        transferTransaction4.setInitiatingUser(user2);
        transferTransaction4.setAmount(new BigDecimal("250.00"));
        transferTransaction4.setMessage("Transfer to savings");
        transferTransaction4.setFromBankAccount(user2CheckingAccount);
        transferTransaction4.setToBankAccount(user2SavingsAccount);

        transactionRepository.save(transferTransaction4);

        TransferTransaction transferTransaction5 = new TransferTransaction();
        transferTransaction5.setInitiatingUser(user4);
        transferTransaction5.setAmount(new BigDecimal("100.00"));
        transferTransaction5.setMessage("Birthday gift");
        transferTransaction5.setFromBankAccount(user4CheckingAccount);
        transferTransaction5.setToBankAccount(user2CheckingAccount);

        transactionRepository.save(transferTransaction5);
    }
}