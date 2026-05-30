package MoneyMachine.services;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import MoneyMachine.mappers.BankAccountMapper;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.services.interfaces.BankAccountService;

@ExtendWith(MockitoExtension.class)
public class BankAccountServiceImplTest {
    @Mock
    private BankAccountRepository bankAccountRepository;

    @Mock
    private BankAccountMapper bankAccountMapper;

    @InjectMocks
    private BankAccountService bankAccountService;

    @Test
    void testGetAllBankAccounts() {
        when(bankAccountRepository.findAll());
    }

    @Test
    void testCloseBankAccount() {

    }
}