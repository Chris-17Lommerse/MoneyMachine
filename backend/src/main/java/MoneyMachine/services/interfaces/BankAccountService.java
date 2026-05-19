package MoneyMachine.services.interfaces;

import MoneyMachine.models.dtos.requests.BankAccountCreationRequest;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.models.User;
import java.util.List;
public interface BankAccountService {
    BankAccountResponse createBankAccountFromRequest(BankAccountCreationRequest bankAccountCreationRequest);
    BankAccountResponse createBankAccountForUser(BankAccountType bankAccountType, User user);
    List<BankAccountResponse> getAllBankAccounts();
}
