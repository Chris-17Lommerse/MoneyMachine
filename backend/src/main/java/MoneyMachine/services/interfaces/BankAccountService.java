package MoneyMachine.services.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

import MoneyMachine.models.BankAccount;

@Service
public interface BankAccountService {
    List<BankAccount> findBankAccountsByUserId(Long id);
}
