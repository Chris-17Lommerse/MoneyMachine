package MoneyMachine.services;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.lang.foreign.Linker.Option;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.h2.mvstore.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springdoc.core.converters.models.Pageable;

import MoneyMachine.mappers.BankAccountMapper;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.models.User;
@ExtendWith(MockitoExtension.class)
public class BankAccountServiceImplTest {
    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @InjectMocks
    private BankAccountServiceImpl bankAccountService;

    private User user;
    private BankAccount bankAccount;
    private Page<BankAccount, Pageable> page;
    private List<BankAccount> bankAccounts;

    @BeforeEach
    void setUp() {
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
    void testGetAllBankAccounts() {
        // when(bankAccountRepository.findAll(pageable);

        // List<BankAccountResponse> bankAccountResponses = bankAccountMapper.toResponseList(bankAccounts);
        // BankAccountOverviewResponse bankAccountOverviewResponse = bankAccountService.getAllBankAccounts(pageable);

        // verify(bankAccountRepository.findAll());
    }

    @Test
    void testCloseBankAccount() {
        when(bankAccountRepository.findById(bankAccount.getIban())).thenReturn(Optional.of(new BankAccount(bankAccount.getIban(), bankAccount.getUser(), bankAccount.getBalance(), bankAccount.getAbsoluteLimit(), bankAccount.getSingleTransferLimit(), bankAccount.getDailyTransferLimit(), bankAccount.getBankAccountType(), bankAccount.getIsActive(), bankAccount.getCreatedAt())));
        bankAccount.setIsActive(false);
        bankAccountRepository.save(bankAccount);
        BankAccountResponse bankAccountResponse = bankAccountMapper.toResponse(bankAccount);
    }
}