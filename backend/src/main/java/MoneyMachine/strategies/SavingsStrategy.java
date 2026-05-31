package MoneyMachine.strategies;

import MoneyMachine.models.BankAccount;
import MoneyMachine.strategies.interfaces.BankAccountTypeStrategy;
import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class SavingsStrategy implements BankAccountTypeStrategy {

    @Override
    public void applyBankAccountRules(BankAccount bankAccount)
    {
        if(bankAccount.getAbsoluteLimit() == null)
        {
            BigDecimal absoluteLimit = BigDecimal.valueOf(20000);
            bankAccount.setAbsoluteLimit(absoluteLimit);
        }

        if (bankAccount.getDailyTransferLimit() == null) {
            BigDecimal dailyTransferLimit = BigDecimal.valueOf(100000);
            bankAccount.setDailyTransferLimit(dailyTransferLimit);
        }
        
        if(bankAccount.getSingleTransferLimit() == null)
        {
            BigDecimal singleTransferLimit = BigDecimal.valueOf(5000);
             bankAccount.setSingleTransferLimit(singleTransferLimit);
        }
    }
}