package MoneyMachine.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import MoneyMachine.exception.NotFoundException;
import MoneyMachine.factories.BankAccountTypeFactory;
import MoneyMachine.factories.IbanGenerator;
import MoneyMachine.mappers.BankAccountMapper;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.models.enums.Role;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.repositories.UserRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository bankAccountRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private BankAccountMapper bankAccountMapper;
    @Mock
    private IbanGenerator ibanGenerator;
    @Mock
    private BankAccountTypeFactory bankAccountTypeFactory;
    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    private User user;
    private BankAccount bankAccount;
    private BankAccountResponse bankAccountResponse;

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
            BigDecimal.ZERO,
            BigDecimal.ZERO,
            new BigDecimal("5000"),
            new BigDecimal("20000"),
            BankAccountType.CHECKING,
            true,
            LocalDateTime.now()
        );

        bankAccountResponse = new BankAccountResponse();
        bankAccountResponse.setIban(bankAccount.getIban());
    }

    @Test
    void getBankAccountByIban_whenIbanExists_returnsBankAccountResponse() {
        
        when(bankAccountRepository.findById(bankAccount.getIban())).thenReturn(Optional.of(bankAccount));
        when(bankAccountMapper.toResponse(bankAccount)).thenReturn(bankAccountResponse);

        BankAccountResponse result = bankAccountService.getBankAccountByIban(bankAccount.getIban());

        assertNotNull(result);
        assertEquals(bankAccount.getIban(), result.getIban());
        verify(bankAccountRepository).findById(bankAccount.getIban());
        verify(bankAccountMapper).toResponse(bankAccount);
    }

    @Test
    void getBankAccountByIban_whenIbanDoesNotExist_throwsNotFoundException() {
        
        when(bankAccountRepository.findById(bankAccount.getIban())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () 
            -> bankAccountService.getBankAccountByIban(bankAccount.getIban())
        );

        verify(bankAccountRepository).findById(bankAccount.getIban());
        verifyNoInteractions(bankAccountMapper);
    }

    @Test
    void getBankAccountByIbanAndUserId_whenIbanAndUserIdMatch_returnsBankAccountResponse() {
        
        when(bankAccountRepository.findByIbanAndUserId(bankAccount.getIban(), user.getId())).thenReturn(Optional.of(bankAccount));
        when(bankAccountMapper.toResponse(bankAccount)).thenReturn(bankAccountResponse);

        BankAccountResponse result = bankAccountService.getBankAccountByIbanAndUserId(bankAccount.getIban(), user.getId());

        assertNotNull(result);
        assertEquals(bankAccount.getIban(), result.getIban());
        verify(bankAccountRepository).findByIbanAndUserId(bankAccount.getIban(), user.getId());
        verify(bankAccountMapper).toResponse(bankAccount);
    }

    @Test
    void getBankAccountByIbanAndUserId_whenIbanDoesNotMatchUserId_throwsNotFoundException() {
       
        when(bankAccountRepository.findByIbanAndUserId(bankAccount.getIban(), user.getId())).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () ->
            bankAccountService.getBankAccountByIbanAndUserId(bankAccount.getIban(), user.getId())
        );

        verify(bankAccountRepository).findByIbanAndUserId(bankAccount.getIban(), user.getId());
        verifyNoInteractions(bankAccountMapper);
    }
}