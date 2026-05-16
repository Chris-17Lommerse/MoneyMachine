package MoneyMachine.services.interfaces;

import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.models.enums.BankAccountType;
import java.math.BigDecimal;
public interface BankAccountService {
    BankAccountResponse createBankAccount(int userId, BankAccountType bankAccountType, BigDecimal absoluteLimit);
}
