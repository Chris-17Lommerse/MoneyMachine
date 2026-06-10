package MoneyMachine.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;

import MoneyMachine.mappers.TransactionMapper;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.User;
import MoneyMachine.models.WithdrawTransaction;
import MoneyMachine.models.dtos.responses.DepositTransactionResponse;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;
import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.models.enums.Role;
import MoneyMachine.policies.TransactionPolicy;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.repositories.TransactionRepository;
import MoneyMachine.services.interfaces.AuthenticationService;
import MoneyMachine.services.interfaces.BankAccountService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class TransactionServiceImplTest {

    @Mock
    private TransactionRepository transactionRepository;
    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private TransactionMapperService mapper;
    @Mock
    private BankAccountService bankAccountService;
    @Mock
    private AuthenticationService authenticationService;
    @Mock
    private TransactionMapper transactionMapper;
    @Mock
    private TransactionPolicy transactionPolicy;
    @InjectMocks
    private TransactionServiceImpl transactionService;

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

        bankAccount = new BankAccount();
        bankAccount.setIban("NL01MONE0123456789");
        bankAccount.setUser(user);
        bankAccount.setBalance(new BigDecimal("1000.00"));
        bankAccount.setAbsoluteLimit(new BigDecimal("0.00"));
        bankAccount.setSingleTransferLimit(new BigDecimal("5000.00"));
        bankAccount.setDailyTransferLimit(new BigDecimal("20000.00"));
        bankAccount.setBankAccountType(BankAccountType.CHECKING);
        bankAccount.setIsActive(true);
    }

    @Test
    void depositAmountIntoBankAccount_whenValidDeposit_returnsDepositTransactionResponse() {

        BigDecimal amount = new BigDecimal("500.00");
        DepositTransactionResponse depositTransactionResponse = new DepositTransactionResponse();

        when(authenticationService.getLoggedInUser()).thenReturn(user);
        when(bankAccountService.getBankAccountEntityByIban(bankAccount.getIban())).thenReturn(bankAccount);
        when(transactionMapper.toDepositTransactionResponse(any(DepositTransaction.class))).thenReturn(depositTransactionResponse);

        DepositTransactionResponse result = transactionService.depositAmountIntoBankAccount(bankAccount.getIban(), amount);

        assertNotNull(result);
        verify(authenticationService).getLoggedInUser();
        verify(bankAccountService).getBankAccountEntityByIban(bankAccount.getIban());
        verify(transactionPolicy).enforceTransactionDepositPolicy(user, amount, bankAccount);
        verify(bankAccountRepository).incrementBalanceByIban(bankAccount.getIban(), amount);
        verify(transactionRepository).save(any(DepositTransaction.class));
        verify(transactionMapper).toDepositTransactionResponse(any(DepositTransaction.class));
    }

    @Test
    void depositAmountIntoBankAccount_whenPolicyViolated_throwsException() {

        BigDecimal amount = new BigDecimal("500.00");

        when(authenticationService.getLoggedInUser()).thenReturn(user);
        when(bankAccountService.getBankAccountEntityByIban(bankAccount.getIban())).thenReturn(bankAccount);
        doThrow(new RuntimeException("Policy violation")).when(transactionPolicy).enforceTransactionDepositPolicy(user, amount, bankAccount);

        assertThrows(RuntimeException.class, () -> 
            transactionService.depositAmountIntoBankAccount(bankAccount.getIban(), amount)
        );

        verify(bankAccountRepository, never()).incrementBalanceByIban(any(), any());
        verify(transactionRepository, never()).save(any());
    }

    @Test
    void withdrawAmountIntoBankAccount_whenValidWithdraw_returnsWithdrawTransactionResponse() {

        BigDecimal amount = new BigDecimal("500.00");
        WithdrawTransactionResponse withdrawTransactionResponse = new WithdrawTransactionResponse();

        when(authenticationService.getLoggedInUser()).thenReturn(user);
        when(bankAccountService.getBankAccountEntityByIban(bankAccount.getIban())).thenReturn(bankAccount);
        when(transactionMapper.toWithdrawTransactionResponse(any(WithdrawTransaction.class))).thenReturn(withdrawTransactionResponse);

        WithdrawTransactionResponse result = transactionService.withdrawAmountIntoBankAccount(bankAccount.getIban(), amount);

        assertNotNull(result);
        verify(authenticationService).getLoggedInUser();
        verify(bankAccountService).getBankAccountEntityByIban(bankAccount.getIban());
        verify(transactionPolicy).enforceTransactionWithdrawPolicy(user, amount, bankAccount);
        verify(bankAccountRepository).decrementBalanceByIban(bankAccount.getIban(), amount);
        verify(transactionRepository).save(any(WithdrawTransaction.class));
        verify(transactionMapper).toWithdrawTransactionResponse(any(WithdrawTransaction.class));
    }

    @Test
    void withdrawAmountIntoBankAccount_whenPolicyViolated_throwsException() {

        BigDecimal amount = new BigDecimal("500.00");

        when(authenticationService.getLoggedInUser()).thenReturn(user);
        when(bankAccountService.getBankAccountEntityByIban(bankAccount.getIban())).thenReturn(bankAccount);
        doThrow(new RuntimeException("Policy violation")).when(transactionPolicy).enforceTransactionWithdrawPolicy(user, amount, bankAccount);

        assertThrows(RuntimeException.class, () -> 
            transactionService.withdrawAmountIntoBankAccount(bankAccount.getIban(), amount)
        );

        verify(bankAccountRepository, never()).decrementBalanceByIban(any(), any());
        verify(transactionRepository, never()).save(any());
    }
}