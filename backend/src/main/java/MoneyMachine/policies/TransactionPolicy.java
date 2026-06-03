package MoneyMachine.policies;

import org.springframework.stereotype.Component;

import MoneyMachine.models.BankAccount;
import MoneyMachine.models.User;

@Component
public class TransactionPolicy {

    public void enforceTransactionPolicy(User user, BankAccount bankAccount) {
        // ENFORCE_ACTION
    }
}
