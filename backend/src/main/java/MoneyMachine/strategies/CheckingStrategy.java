package MoneyMachine.strategies;

import MoneyMachine.models.BankAccount;
import MoneyMachine.strategies.interfaces.BankAccountTypeStrategy;

public class CheckingStrategy implements BankAccountTypeStrategy {
    public void bankAccountRules(BankAccount bankAccount)
    {
        bankAccount.getAbsoluteLimit();
    }
}
