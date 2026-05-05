package MoneyMachine;

import java.math.BigDecimal;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.Transaction;
import MoneyMachine.models.TransferTransaction;
import MoneyMachine.models.WithdrawTransaction;
import MoneyMachine.repositories.TransactionRepository;

@Component
public class DataSeeder implements ApplicationRunner {

    private final TransactionRepository transactionRepository;

    public DataSeeder(TransactionRepository transactionRepository){
        this.transactionRepository = transactionRepository;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {

        DepositTransaction depositTransaction = new DepositTransaction(1, new BigDecimal("10"), "Hello deposit!", true, "NL91ABNA0417164300");
        transactionRepository.save(depositTransaction);

        WithdrawTransaction withdrawTransaction = new WithdrawTransaction(1, new BigDecimal("10"), "Hello withdraw!", true, "NL91ABNA0417164300");
        transactionRepository.save(withdrawTransaction);

        TransferTransaction transferTransaction = new TransferTransaction(1, new BigDecimal("10"), "Hello transfer!", true, "NL91ABNA0417164300", "NL91ABNA0417164300");
        transactionRepository.save(transferTransaction);

        for (Transaction transaction : transactionRepository.findAll()) {
            System.out.println(transaction.getTransactionId());
            System.out.println(transaction.getClass());
            System.out.println(String.format("This subclass is a: %s!", transaction.getClass().getSimpleName()));
        }
    }
}
