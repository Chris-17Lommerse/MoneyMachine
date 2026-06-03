package MoneyMachine.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.List;

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
import MoneyMachine.services.interfaces.AuthenticationService;
import MoneyMachine.models.User;
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
    
    private AuthenticationService authenticationService;
    private BankAccount bankAccount;
    private User user;
    private Page<BankAccount> page;
    private BankAccountMapper bankAccountMapper;

    @BeforeEach
    void setUp()
    {
        user = new User();
        user.setFirstName("employeeFirstName");
        user.setLastName("employeeLastName");
        user.setEmail("employee@employee.employee");
        user.setBsn("123456749");
        user.setPhoneNumber("+31 6 12 34 54 78");
        user.setRole(Role.EMPLOYEE);
        user.setPassword(authenticationService.getHashedPassword("password"));
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
    }

    @Test
    public void closeBankAccount_whenBankAccountIsClosed_setIsActiveFalseAndReturnUpdatedBankAccount()
    {
        
    }

    @Test
    public void findAll_whenBankAccountsFound_returnsAllBankAccounts(Pageable pageable)
    {
        List<BankAccount> bankAccounts = page.getContent();
        when(bankAccountRepository.findAll()).thenReturn(bankAccounts);

        List<BankAccountResponse> items = bankAccountMapper.toResponseList(bankAccounts);
        BankAccountOverviewResponse bankAccountOverviewResponse = new BankAccountOverviewResponse(items, page.getNumber(), page.getSize());

        bankAccountOverviewResponse = bankAccountServiceImpl.getAllBankAccounts(pageable);
        assertEquals(bankAccounts, bankAccountOverviewResponse);

        verify(bankAccountRepository.findAll());
    }

    public void testGetAllBankAccountsByUserId()
    {
        
    }

    public void testGetBankAccountByIbanAndUserId()
    {

    }
}
