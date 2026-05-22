package MoneyMachine.strategies.interfaces;

import org.springframework.stereotype.Component;

import MoneyMachine.models.BankAccount;

@Component
public interface BankAccountTypeStrategy {
    void applyBankAccountRules(BankAccount BankAccount);
}
