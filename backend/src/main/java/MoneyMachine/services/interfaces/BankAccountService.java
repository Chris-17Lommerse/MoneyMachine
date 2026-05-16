package MoneyMachine.services.interfaces;

import MoneyMachine.models.dtos.requests.BankAccountCreationRequest;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
public interface BankAccountService {
    BankAccountResponse createBankAccount(BankAccountCreationRequest bankAccountCreationRequest);
}
