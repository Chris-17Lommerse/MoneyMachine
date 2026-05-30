package MoneyMachine.mappers;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.User;
import MoneyMachine.models.enums.BankAccountType;
public class BankAccountMapperTest {
    private List<BankAccount> bankAccounts;
    private BankAccount bankAccount;
    private User user;

    @BeforeEach
    void setUp() {
        bankAccount = new BankAccount();
        user = new User();
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
        BankAccountMapper bankAccountMapper = new BankAccountMapper();
        bankAccountMapper.toResponse(bankAccount);
        assertNotNull(bankAccountMapper);
        assertNotEquals(bankAccount.getIban(), bankAccountMapper.toResponse(bankAccount).getIban());
        assertNotEquals(bankAccount.getUser().getId(), bankAccountMapper.toResponse(bankAccount).getUserId());
        assertNotEquals(bankAccount.getAbsoluteLimit(), bankAccountMapper.toResponse(bankAccount).getAbsoluteLimit());
    }

    @Test
    void toResponseListShouldNotBeNull()
    {
        BankAccountMapper bankAccountMapper = new BankAccountMapper();
        bankAccountMapper.toResponseList(bankAccounts);
        assertNotEquals(bankAccountMapper.toResponseList(bankAccounts).size(), 0);
    }
}
