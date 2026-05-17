package MoneyMachine.strategies;

import MoneyMachine.models.BankAccount;
import MoneyMachine.strategies.interfaces.BankAccountTypeStrategy;

public class CheckingStrategy implements BankAccountTypeStrategy {
    public void applyBankAccountRules(BankAccount bankAccount)
    {
        bankAccount.getAbsoluteLimit();
    }
}
