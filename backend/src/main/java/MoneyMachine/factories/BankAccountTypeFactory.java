package MoneyMachine.factories;

import java.util.HashMap;
import java.util.List;

import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.strategies.CheckingStrategy;
import MoneyMachine.strategies.SavingsStrategy;
import MoneyMachine.strategies.interfaces.BankAccountTypeStrategy;
import java.util.Map;


public class BankAccountTypeFactory {
    private final Map<BankAccountType, BankAccountTypeStrategy> strategies = new HashMap<>();
    
    public BankAccountTypeFactory(List<BankAccountTypeStrategy> strategyBeans)
    {
        for(BankAccountTypeStrategy strategy : strategyBeans)
        {
            if(strategy instanceof CheckingStrategy)
            {
                strategies.put(BankAccountType.CHECKING, strategy);
            }

            else if(strategy instanceof SavingsStrategy)
            {
                strategies.put(BankAccountType.SAVINGS, strategy);
            }
        }
    }

    public BankAccountTypeStrategy getStrategy(BankAccountType bankAccountType)
    {
        BankAccountTypeStrategy strategy = strategies.get(bankAccountType);

        if(strategy == null)
        {
            throw new IllegalArgumentException("No strategy found for: " + bankAccountType);
        }

        return strategy;
    }
}