package MoneyMachine.strategies;

import MoneyMachine.models.BankAccount;
import MoneyMachine.strategies.interfaces.BankAccountTypeStrategy;
import java.math.BigDecimal;

public class SavingsStrategy implements BankAccountTypeStrategy {
    private static BigDecimal maxSingleTransferLimit; 
    public void bankAccountRules(BankAccount bankAccount)
    {
        bankAccount.setSingleTransferLimit(maxSingleTransferLimit);
    }
}