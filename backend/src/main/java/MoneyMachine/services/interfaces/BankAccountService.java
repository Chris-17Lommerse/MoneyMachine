package MoneyMachine.services.interfaces;

import MoneyMachine.models.dtos.requests.BankAccountCreationRequest;
import MoneyMachine.models.dtos.requests.PatchRequest;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import MoneyMachine.models.dtos.responses.BankAccountResponse;

import org.springframework.data.domain.Pageable;

import MoneyMachine.models.BankAccount;

public interface BankAccountService {
    BankAccountResponse createBankAccountFromRequest(BankAccountCreationRequest bankAccountCreationRequest);
    BankAccountOverviewResponse getAllBankAccounts(Pageable pageable);
    BankAccountResponse closeBankAccount(PatchRequest patchRequest, String iban);
    BankAccountOverviewResponse getAllBankAccountsByUserId(Long id, Pageable pageable);
    BankAccountResponse getBankAccountByIban(String iban);
    BankAccountResponse getBankAccountByIbanAndUserId(String iban, Long id);
    BankAccount getBankAccountEntityByIban(String iban);
}
