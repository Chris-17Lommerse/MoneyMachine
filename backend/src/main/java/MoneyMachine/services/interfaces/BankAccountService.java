package MoneyMachine.services.interfaces;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import MoneyMachine.models.BankAccount;

@Service
public interface BankAccountService {
    List<BankAccount> findBankAccountsByUserId(Long id);
    BankAccount getBankAccountByIban(String iban);
    BankAccount getBankAccountByIbanAndUserId(String iban, Long id);
    void setBankAccountBalance(String iban, BigDecimal newBalance);
}
