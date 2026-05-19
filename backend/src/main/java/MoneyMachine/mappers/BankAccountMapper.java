package MoneyMachine.mappers;

import org.springframework.stereotype.Component;

import MoneyMachine.models.BankAccount;
import MoneyMachine.models.dtos.responses.BankAccountResponse;

@Component
public class BankAccountMapper {
    
    public BankAccountResponse toResponse(BankAccount bankAccount)
    {
        BankAccountResponse bankAccountResponse = new BankAccountResponse();
        bankAccountResponse.setBalance(bankAccount.getBalance());
        bankAccountResponse.setAbsoluteLimit(bankAccount.getAbsoluteLimit());
        bankAccountResponse.setBankAccountType(bankAccount.getBankAccountType());
        bankAccountResponse.setSingleTransferLimit(bankAccount.getSingleTransferLimit());
        bankAccountResponse.setDailyTransferLimit(bankAccount.getDailyTransferLimit());
        bankAccountResponse.setActive(bankAccount.getIsActive());
        return bankAccountResponse;
    }
}
