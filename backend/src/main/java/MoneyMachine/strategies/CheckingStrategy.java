package MoneyMachine.strategies;

import org.springframework.stereotype.Component;
import MoneyMachine.models.BankAccount;
import MoneyMachine.strategies.interfaces.BankAccountTypeStrategy;
@Component
public class CheckingStrategy implements BankAccountTypeStrategy {
    @Override
    public void applyBankAccountRules(BankAccount bankAccount)
    {
        bankAccount.getAbsoluteLimit();
    }
}
