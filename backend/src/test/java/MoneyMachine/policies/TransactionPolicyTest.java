package MoneyMachine.policies;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import MoneyMachine.exception.NotAuthorizedException;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.User;
import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.models.enums.Role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionPolicyTest {

    @InjectMocks
    private TransactionPolicy transactionPolicy;

    private User user;
    private BankAccount bankAccount;

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

        bankAccount = new BankAccount(
            "NL01MONE0123456789",
            user,
            new BigDecimal("1000.00"),
            new BigDecimal("0.00"),
            new BigDecimal("5000.00"),
            new BigDecimal("20000.00"),
            BankAccountType.CHECKING,
            true,
            LocalDateTime.now()
        );
    }

    @Test
    void enforceTransactionPolicy_whenValidUserAndAmount_doesNotThrow() {

        assertDoesNotThrow(() ->
            transactionPolicy.enforceTransactionPolicy(user, new BigDecimal("100.00"), bankAccount)
        );
    }

    @Test
    void enforceTransactionPolicy_whenUserIsNotApproved_throwsNotAuthorizedException() {

        user.setIsApproved(false);

        assertThrows(NotAuthorizedException.class, () ->
            transactionPolicy.enforceTransactionPolicy(user, new BigDecimal("100.00"), bankAccount)
        );
    }

    @Test
    void enforceTransactionPolicy_whenAmountIsZero_throwsIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () ->
            transactionPolicy.enforceTransactionPolicy(user, new BigDecimal("0.00"), bankAccount)
        );
    }

    @Test
    void enforceTransactionPolicy_whenAmountIsNegative_throwsIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () ->
            transactionPolicy.enforceTransactionPolicy(user, new BigDecimal("-100.00"), bankAccount)
        );
    }

    @Test
    void enforceTransactionPolicy_whenAmountExceedsSingleTransferLimit_throwsIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () ->
            transactionPolicy.enforceTransactionPolicy(user, new BigDecimal("6000.00"), bankAccount)
        );
    }

    @Test
    void enforceTransactionWithdrawPolicy_whenValidWithdraw_doesNotThrow() {

        assertDoesNotThrow(() ->
            transactionPolicy.enforceTransactionWithdrawPolicy(user, new BigDecimal("100.00"), bankAccount)
        );
    }

    @Test
    void enforceTransactionWithdrawPolicy_whenAmountExceedsAbsoluteLimit_throwsIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () ->
            transactionPolicy.enforceTransactionWithdrawPolicy(user, new BigDecimal("1500.00"), bankAccount)
        );
    }

    @Test
    void enforceTransactionWithdrawPolicy_whenAmountEqualsBalance_doesNotThrow() {

        assertDoesNotThrow(() ->
            transactionPolicy.enforceTransactionWithdrawPolicy(user, new BigDecimal("1000.00"), bankAccount)
        );
    }

    @Test
    void enforceTransactionWithdrawPolicy_whenUserIsNotApproved_throwsNotAuthorizedException() {

        user.setIsApproved(false);

        assertThrows(NotAuthorizedException.class, () ->
            transactionPolicy.enforceTransactionWithdrawPolicy(user, new BigDecimal("100.00"), bankAccount)
        );
    }

    @Test
    void enforceTransactionWithdrawPolicy_whenAmountIsNegative_throwsIllegalArgumentException() {

        assertThrows(IllegalArgumentException.class, () ->
            transactionPolicy.enforceTransactionWithdrawPolicy(user, new BigDecimal("-100.00"), bankAccount)
        );
    }
}