package MoneyMachine.mappers;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import MoneyMachine.models.BankAccount;
import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.User;
import MoneyMachine.models.WithdrawTransaction;
import MoneyMachine.models.dtos.responses.DepositTransactionResponse;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;
import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.models.enums.Role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionMapperTest {

    @InjectMocks
    private TransactionMapper transactionMapper;

    private User user;
    private BankAccount bankAccount;
    private DepositTransaction depositTransaction;
    private WithdrawTransaction withdrawTransaction;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("John");
        user.setLastName("Doe");
        user.setEmail("john@doe.com");
        user.setBsn("123456789");
        user.setPhoneNumber("+31 6 12 34 56 78");
        user.setRole(Role.USER);
        user.setPassword("mockPassword");
        user.setIsActive(true);
        user.setIsApproved(true);

        bankAccount = new BankAccount();
        bankAccount.setIban("NL01MONE0123456789");
        bankAccount.setUser(user);
        bankAccount.setBalance(new BigDecimal("1000.00"));
        bankAccount.setAbsoluteLimit(new BigDecimal("0.00"));
        bankAccount.setSingleTransferLimit(new BigDecimal("5000.00"));
        bankAccount.setDailyTransferLimit(new BigDecimal("20000.00"));
        bankAccount.setBankAccountType(BankAccountType.CHECKING);
        bankAccount.setIsActive(true);

        depositTransaction = new DepositTransaction();
        depositTransaction.setInitiatingUser(user);
        depositTransaction.setAmount(new BigDecimal("100.00"));
        depositTransaction.setMessage("ATM deposit");
        depositTransaction.setDateTime(LocalDateTime.now());
        depositTransaction.setToBankAccount(bankAccount);

        withdrawTransaction = new WithdrawTransaction();
        withdrawTransaction.setInitiatingUser(user);
        withdrawTransaction.setAmount(new BigDecimal("100.00"));
        withdrawTransaction.setMessage("ATM withdraw");
        withdrawTransaction.setDateTime(LocalDateTime.now());
        withdrawTransaction.setFromBankAccount(bankAccount);
    }

    @Test
    void toDepositTransactionResponse_whenValidDepositTransaction_mapsAllProperties() {

        DepositTransactionResponse result = transactionMapper.toDepositTransactionResponse(depositTransaction);

        assertNotNull(result);
        assertEquals(result.getTransactionId(), depositTransaction.getTransactionId());
        assertEquals(result.getInitiatingUserId(), depositTransaction.getInitiatingUser().getId());
        assertEquals(result.getToAccountIban(), depositTransaction.getToBankAccount().getIban());
        assertEquals(result.getMessage(), depositTransaction.getMessage());
        assertEquals(result.getAmount(), depositTransaction.getAmount());
        assertEquals(result.getDateTime(), depositTransaction.getDateTime());
    }

    @Test
    void toWithdrawTransactionResponse_whenValidWithdrawTransaction_mapsAllProperties() {

        WithdrawTransactionResponse result = transactionMapper.toWithdrawTransactionResponse(withdrawTransaction);

        assertNotNull(result);
        assertEquals(result.getTransactionId(), withdrawTransaction.getTransactionId());
        assertEquals(result.getInitiatingUserId(), withdrawTransaction.getInitiatingUser().getId());
        assertEquals(result.getFromAccountIban(), withdrawTransaction.getFromBankAccount().getIban());
        assertEquals(result.getMessage(), withdrawTransaction.getMessage());
        assertEquals(result.getAmount(), withdrawTransaction.getAmount());
        assertEquals(result.getDateTime(), withdrawTransaction.getDateTime());
    }
}