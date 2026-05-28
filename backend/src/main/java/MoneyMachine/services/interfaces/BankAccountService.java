package MoneyMachine.services.interfaces;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import MoneyMachine.models.BankAccount;
import MoneyMachine.models.dtos.requests.BankAccountCreationRequest;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.models.enums.BankAccountType;

import org.springframework.data.domain.Pageable;

import MoneyMachine.models.User;

@Service
public interface BankAccountService {
    BankAccountResponse createBankAccountFromRequest(BankAccountCreationRequest bankAccountCreationRequest);
    BankAccountResponse createBankAccountForUser(BankAccountType bankAccountType, User user);
    BankAccountOverviewResponse getAllBankAccounts(Pageable pageable);
    BankAccountOverviewResponse getAllBankAccountsByUserId(Long id);
    BankAccount getBankAccountByIban(String iban);
    BankAccount getBankAccountByIbanAndUserId(String iban, Long id);
    void setBankAccountBalance(String iban, BigDecimal newBalance);
}
