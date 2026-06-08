package MoneyMachine.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.math.BigDecimal;
import java.util.Optional;

import MoneyMachine.exception.NotFoundException;
import MoneyMachine.factories.BankAccountTypeFactory;
import MoneyMachine.factories.IbanGenerator;
import MoneyMachine.mappers.BankAccountMapper;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.requests.PatchRequest;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
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
import java.util.List;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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
    private BankAccount savingsBankAccount;
    private BankAccountResponse bankAccountResponse;
    private List<BankAccount> bankAccounts;
    private PatchRequest patchRequest;

    @Mock
    private Page<BankAccount> page;
    @Mock
    private Pageable pageable;

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

        patchRequest = new PatchRequest();

        bankAccount = new BankAccount();
        bankAccount.setIban("NL01MONE0123456789");
        bankAccount.setUser(user);
        bankAccount.setBalance(new BigDecimal("1000.00"));
        bankAccount.setAbsoluteLimit(new BigDecimal("0.00"));
        bankAccount.setSingleTransferLimit(new BigDecimal("5000.00"));
        bankAccount.setDailyTransferLimit(new BigDecimal("20000.00"));
        bankAccount.setBankAccountType(BankAccountType.CHECKING);
        bankAccount.setIsActive(true);

        savingsBankAccount = new BankAccount();
        savingsBankAccount.setIban("NL91ABN2417164300");
        savingsBankAccount.setUser(user);
        savingsBankAccount.setBalance(new BigDecimal("300"));
        savingsBankAccount.setAbsoluteLimit(new BigDecimal("0"));
        savingsBankAccount.setSingleTransferLimit(new BigDecimal("1000"));
        savingsBankAccount.setDailyTransferLimit(new BigDecimal("2000"));
        savingsBankAccount.setBankAccountType(BankAccountType.SAVINGS);
        savingsBankAccount.setIsActive(true);

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

        assertThrows(NotFoundException.class, () -> bankAccountService.getBankAccountByIban(bankAccount.getIban()));

        verify(bankAccountRepository).findById(bankAccount.getIban());
        verifyNoInteractions(bankAccountMapper);
    }

    @Test
    void getBankAccountByIbanAndUserId_whenIbanAndUserIdMatch_returnsBankAccountResponse() {

        when(bankAccountRepository.findByIbanAndUserId(bankAccount.getIban(), user.getId()))
                .thenReturn(Optional.of(bankAccount));
        when(bankAccountMapper.toResponse(bankAccount)).thenReturn(bankAccountResponse);

        BankAccountResponse result = bankAccountService.getBankAccountByIbanAndUserId(bankAccount.getIban(),
                user.getId());

        assertNotNull(result);
        assertEquals(bankAccount.getIban(), result.getIban());
        verify(bankAccountRepository).findByIbanAndUserId(bankAccount.getIban(), user.getId());
        verify(bankAccountMapper).toResponse(bankAccount);
    }

    @Test
    void getBankAccountByIbanAndUserId_whenIbanDoesNotMatchUserId_throwsNotFoundException() {

        when(bankAccountRepository.findByIbanAndUserId(bankAccount.getIban(), user.getId()))
                .thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> bankAccountService.getBankAccountByIbanAndUserId(bankAccount.getIban(), user.getId()));

        verify(bankAccountRepository).findByIbanAndUserId(bankAccount.getIban(), user.getId());
        verifyNoInteractions(bankAccountMapper);
    }

 @Test
    public void closeBankAccount_whenBankAccountIsClosed_setIsActiveFalseAndReturnUpdatedBankAccount() {
        String iban = bankAccount.getIban();
        when(bankAccountRepository.findById(iban)).thenReturn(Optional.of(bankAccount));

        BankAccountResponse expectedResponse = new BankAccountResponse();
        expectedResponse.setActive(false);

        when(bankAccountMapper.toResponse(bankAccount)).thenReturn(expectedResponse);
        expectedResponse = bankAccountService.closeBankAccount(patchRequest,
                iban);

        assertNotNull(expectedResponse);
        
        verify(bankAccountRepository).findById(iban);
        verify(bankAccountRepository).save(bankAccount);
        verify(bankAccountMapper).toResponse(bankAccount);
    }

    @Test
    public void getAllBankAccounts_whenBankAccountsFound_returnsAllBankAccounts() {
        bankAccounts = List.of(bankAccount, savingsBankAccount);
        when(bankAccountRepository.findAll(pageable)).thenReturn(page);
        when(page.getContent()).thenReturn(bankAccounts);
        when(page.getNumber()).thenReturn(0);
        when(page.getSize()).thenReturn(2);

        List<BankAccountResponse> expectedBankAccounts = List.of(
                new BankAccountResponse(),
                new BankAccountResponse());

        when(bankAccountMapper.toResponseList(bankAccounts)).thenReturn(expectedBankAccounts);

        BankAccountOverviewResponse bankAccountOverviewResponse = bankAccountService.getAllBankAccounts(pageable);
        assertEquals(2, bankAccountOverviewResponse.getItems().size());
        assertEquals(0, bankAccountOverviewResponse.getPage());
        assertEquals(2, bankAccountOverviewResponse.getPageSize());
        assertNotNull(bankAccountOverviewResponse);
        verify(bankAccountRepository).findAll(pageable);
        verify(bankAccountMapper).toResponseList(bankAccounts);
    }
}