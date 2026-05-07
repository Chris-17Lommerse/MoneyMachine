package MoneyMachine;

import java.math.BigDecimal;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import MoneyMachine.models.BankAccount;
import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.Transaction;
import MoneyMachine.models.TransferTransaction;
import MoneyMachine.models.User;
import MoneyMachine.models.WithdrawTransaction;
import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.models.enums.Role;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.repositories.TransactionRepository;
import MoneyMachine.repositories.UserRepository;

@Component
public class DataSeeder implements ApplicationRunner {

    private final BankAccountRepository bankAccountRepository;
    private final UserRepository userRepository;
    private final TransactionRepository transactionRepository;

    public DataSeeder(TransactionRepository transactionRepository, UserRepository userRepository, BankAccountRepository bankAccountRepository){
        this.transactionRepository = transactionRepository;
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        User user = new User("testFirstName", "testLastName", "user@user.user", "123456789", "+31 6 12 34 56 78", Role.user, true, true);
        userRepository.save(user);

        BankAccount bankAccount = new BankAccount("NL91ABNA0417164300", user, new BigDecimal("100"), new BigDecimal("-100"), new BigDecimal("100"), new BigDecimal("100"), BankAccountType.checking, true);
        bankAccountRepository.save(bankAccount);

        DepositTransaction depositTransaction = new DepositTransaction(user, new BigDecimal("10"), "Hello deposit!", true, bankAccount);
        transactionRepository.save(depositTransaction);

        WithdrawTransaction withdrawTransaction = new WithdrawTransaction(user, new BigDecimal("10"), "Hello withdraw!", true, bankAccount);
        transactionRepository.save(withdrawTransaction);

        TransferTransaction transferTransaction = new TransferTransaction(user, new BigDecimal("10"), "Hello transfer!", true, bankAccount, bankAccount);
        transactionRepository.save(transferTransaction);

        for (Transaction transaction : transactionRepository.findAll()) {
            System.out.println(transaction.getTransactionId());
            System.out.println(transaction.getClass());
            System.out.println(String.format("This subclass is a: %s!", transaction.getClass().getSimpleName()));
        }
    }
}
