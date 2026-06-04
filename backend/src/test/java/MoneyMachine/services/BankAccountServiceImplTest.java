package MoneyMachine.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;

import MoneyMachine.mappers.BankAccountMapper;
import MoneyMachine.models.BankAccount;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.requests.PatchRequest;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.models.enums.Role;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceImplTest {

    @Mock
    private BankAccountRepository bankAccountRepository;

    @InjectMocks
    private BankAccountServiceImpl bankAccountServiceImpl;

    @InjectMocks
    private AuthenticationServiceImpl authenticationServiceImpl;
    private BankAccount bankAccount;
    private User user;
    private Optional<BankAccount> optionalBankAccount;
    private Page<BankAccount> page;

    @Mock
    private BankAccountMapper bankAccountMapper;

    private PatchRequest patchRequest;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("employeeFirstName");
        user.setLastName("employeeLastName");
        user.setEmail("employee@employee.employee");
        user.setBsn("123456749");
        user.setPhoneNumber("+31 6 12 34 54 78");
        user.setRole(Role.EMPLOYEE);
        user.setPassword(authenticationServiceImpl.getHashedPassword("password"));
        user.setIsActive(false);
        user.setIsApproved(false);
        bankAccount = new BankAccount();
        bankAccount.setIban("NL91ABNA0417164300");
        bankAccount.setUser(user);
        bankAccount.setBalance(new BigDecimal("100"));
        bankAccount.setAbsoluteLimit(new BigDecimal("-100"));
        bankAccount.setSingleTransferLimit(new BigDecimal("100"));
        bankAccount.setDailyTransferLimit(new BigDecimal("100"));
        bankAccount.setBankAccountType(BankAccountType.CHECKING);
        bankAccount.setIsActive(true);
        patchRequest = new PatchRequest();
        patchRequest.setActive(false);

        optionalBankAccount = Optional.of(bankAccount);
    }

    @Test
    public void closeBankAccount_whenBankAccountIsClosed_setIsActiveFalseAndReturnUpdatedBankAccount() {
        when(bankAccountRepository.findById(bankAccount.getIban())).thenReturn(optionalBankAccount);

        when(bankAccountMapper.toResponse(bankAccount));
        BankAccountResponse bankAccountResponse = bankAccountServiceImpl.closeBankAccount(patchRequest,
                bankAccount.getIban());

        assertEquals(bankAccount, bankAccountResponse);
        verify(bankAccountRepository.save(bankAccount));
    }

    @Test
    public void findAll_whenBankAccountsFound_returnsAllBankAccounts(Pageable pageable) {
        List<BankAccount> bankAccounts = page.getContent();
        when(bankAccountRepository.findAll()).thenReturn(bankAccounts);

        List<BankAccountResponse> items = bankAccountMapper.toResponseList(bankAccounts);
        BankAccountOverviewResponse bankAccountOverviewResponse = new BankAccountOverviewResponse(items,
                page.getNumber(), page.getSize());

        bankAccountOverviewResponse = bankAccountServiceImpl.getAllBankAccounts(pageable);
        assertEquals(bankAccounts, bankAccountOverviewResponse);

        verify(bankAccountRepository.findAll());
    }

    public void testGetAllBankAccountsByUserId() {

    }

    @Test
    void testGetBankAccountByIban() {

    }

    @Test
    void testGetBankAccountByIbanAndUserId() {

    }

    @Test
    void testSetBankAccountBalance() {

    }
}
