package MoneyMachine.services.interfaces;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import MoneyMachine.models.dtos.requests.BankAccountCreationRequest;
import MoneyMachine.models.dtos.requests.PatchRequest;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.models.enums.BankAccountType;

import org.springframework.data.domain.Pageable;

import MoneyMachine.models.BankAccount;
import MoneyMachine.models.User;

@Service
public interface BankAccountService {
    BankAccountResponse createBankAccountFromRequest(BankAccountCreationRequest bankAccountCreationRequest);
    void createBankAccountForUser(BankAccountType bankAccountType, User user);
    BankAccountOverviewResponse getAllBankAccounts(Pageable pageable);
    BankAccountResponse closeBankAccount(PatchRequest patchRequest, String iban);
    BankAccountOverviewResponse getAllBankAccountsByUserId(Long id, Pageable pageable);
    BankAccountResponse getBankAccountByIban(String iban);
    BankAccountResponse getBankAccountByIbanAndUserId(String iban, Long id);
    BankAccount findBankAccountEntityByIban(String iban);
    void setBankAccountBalance(String iban, BigDecimal newBalance);
}
