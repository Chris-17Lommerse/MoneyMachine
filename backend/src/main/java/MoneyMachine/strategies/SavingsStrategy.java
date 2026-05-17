package MoneyMachine.strategies;

import MoneyMachine.models.BankAccount;
import MoneyMachine.strategies.interfaces.BankAccountTypeStrategy;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class SavingsStrategy implements BankAccountTypeStrategy {
    private static BigDecimal maxSingleTransferLimit; 
    public void applyBankAccountRules(BankAccount bankAccount)
    {
        bankAccount.setSingleTransferLimit(maxSingleTransferLimit);
    }
}