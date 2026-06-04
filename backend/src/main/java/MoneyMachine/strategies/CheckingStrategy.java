package MoneyMachine.strategies;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;
import MoneyMachine.models.BankAccount;
import MoneyMachine.strategies.interfaces.BankAccountTypeStrategy;
@Component
public class CheckingStrategy implements BankAccountTypeStrategy {

    @Override
    public void applyBankAccountRules(BankAccount bankAccount)
    {
        if(bankAccount.getAbsoluteLimit() == null)
        {
            BigDecimal absoluteLimit = BigDecimal.valueOf(-100);
            bankAccount.setAbsoluteLimit(absoluteLimit);
        }

        if (bankAccount.getDailyTransferLimit() == null) {
            BigDecimal dailyTransferLimit = BigDecimal.valueOf(20000);
            bankAccount.setDailyTransferLimit(dailyTransferLimit);
        }
        
        if(bankAccount.getSingleTransferLimit() == null)
        {
            BigDecimal singleTransferLimit = BigDecimal.valueOf(5000);
            bankAccount.setSingleTransferLimit(singleTransferLimit);
        }
    }
}
