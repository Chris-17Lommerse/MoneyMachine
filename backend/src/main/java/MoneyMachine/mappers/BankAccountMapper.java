package MoneyMachine.mappers;

import org.springframework.stereotype.Component;

import MoneyMachine.models.BankAccount;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.models.User;

import java.util.ArrayList;
import java.util.List;
@Component
public class BankAccountMapper {
    
    private final UserMapper userMapper;
    private final User user;

    BankAccountMapper(UserMapper userMapper, User user) {
        this.userMapper = userMapper;
        this.user = user;
    }

    public BankAccountResponse toResponse(BankAccount bankAccount)
    {
        BankAccountResponse bankAccountResponse = new BankAccountResponse();
        bankAccountResponse.setIban(bankAccount.getIban());
        bankAccountResponse.setUserId(userMapper.toResponse(user).getUserId());
        bankAccountResponse.setBalance(bankAccount.getBalance());
        bankAccountResponse.setAbsoluteLimit(bankAccount.getAbsoluteLimit());
        bankAccountResponse.setBankAccountType(bankAccount.getBankAccountType());
        bankAccountResponse.setSingleTransferLimit(bankAccount.getSingleTransferLimit());
        bankAccountResponse.setDailyTransferLimit(bankAccount.getDailyTransferLimit());
        bankAccountResponse.setActive(bankAccount.getIsActive());
        return bankAccountResponse;
    }

    public List<BankAccountResponse> toDTOList(List<BankAccount> bankAccountList)
    {
        List<BankAccountResponse> bankAccountResponses = new ArrayList<BankAccountResponse>();

        for(BankAccount bankAccount: bankAccountList)
        {
            bankAccountResponses.add(toResponse(bankAccount));
        }

        return bankAccountResponses;
    }
}
