package MoneyMachine.mappers;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.models.enums.BankAccountType;
public class BankAccountMapperTest {
    private List<BankAccount> bankAccounts;
    private BankAccount bankAccount;
    private User user;
    private BankAccountMapper bankAccountMapper;

    @BeforeEach
    void setUp() {
        bankAccountMapper = new BankAccountMapper();
        bankAccount = new BankAccount();
        bankAccounts = new ArrayList<BankAccount>();
;       user = new User();
        user.setId(1L);
        bankAccount.setIban("NL91ABNA0417164300");
        bankAccount.setUser(user);
        bankAccount.setBalance(BigDecimal.valueOf(200));
        bankAccount.setAbsoluteLimit(BigDecimal.valueOf(200));
        bankAccount.setSingleTransferLimit(BigDecimal.valueOf(200));
        bankAccount.setDailyTransferLimit(BigDecimal.valueOf(200));
        bankAccount.setBankAccountType(BankAccountType.CHECKING);
        bankAccount.setIsActive(true);
    }

    @Test
    void toResponseShouldNotBeNull()
    {
        BankAccountResponse bankAccountResponse = bankAccountMapper.toResponse(bankAccount);
        assertEquals(bankAccount.getIban(), bankAccountResponse.getIban());
        assertEquals(user.getId(), bankAccountResponse.getUserId());
        assertEquals(bankAccount.getAbsoluteLimit(), bankAccountResponse.getAbsoluteLimit());
        assertEquals(bankAccount.getBalance(), bankAccountResponse.getBalance());
        assertEquals(bankAccount.getBankAccountType(), bankAccountResponse.getBankAccountType());
        assertEquals(bankAccount.getIsActive(), bankAccountResponse.isActive());
        assertEquals(bankAccount.getSingleTransferLimit(), bankAccountResponse.getSingleTransferLimit());
        assertEquals(bankAccount.getDailyTransferLimit(), bankAccountResponse.getDailyTransferLimit());
    }

    @Test
    void toResponseListShouldNotBeNull()
    {   
        bankAccounts.add(bankAccount);
        List<BankAccountResponse> bankAccountResponses = bankAccountMapper.toResponseList(bankAccounts);
        assertEquals(bankAccountResponses.size(), 1);
        assertEquals(bankAccountResponses.getFirst().getIban(), bankAccount.getIban());
        assertEquals(bankAccountResponses.getFirst().getUserId(), bankAccount.getUser().getId());
    }
}
