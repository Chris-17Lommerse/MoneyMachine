package MoneyMachine.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import MoneyMachine.models.BankAccount;
import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.repositories.BankAccountRepository;

public class BankAccountControllerTest extends BaseControllerTest {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private BankAccount userBankAccount;
    private BankAccount employeeBankAccount;

    @BeforeEach
    void setUp() {
        super.setUpMockAuth();

        userBankAccount = new BankAccount();
        userBankAccount.setIban("NL91ABNA0417164300");
        userBankAccount.setUser(user);
        userBankAccount.setBalance(new BigDecimal("1000.00"));
        userBankAccount.setAbsoluteLimit(new BigDecimal("0.00"));
        userBankAccount.setSingleTransferLimit(new BigDecimal("5000.00"));
        userBankAccount.setDailyTransferLimit(new BigDecimal("20000.00"));
        userBankAccount.setBankAccountType(BankAccountType.CHECKING);
        userBankAccount.setIsActive(true);

        bankAccountRepository.save(userBankAccount);

        employeeBankAccount = new BankAccount();
        employeeBankAccount.setIban("NL47ABNA0428395174");
        employeeBankAccount.setUser(employee);
        employeeBankAccount.setBalance(new BigDecimal("1000.00"));
        employeeBankAccount.setAbsoluteLimit(new BigDecimal("0.00"));
        employeeBankAccount.setSingleTransferLimit(new BigDecimal("5000.00"));
        employeeBankAccount.setDailyTransferLimit(new BigDecimal("20000.00"));
        employeeBankAccount.setBankAccountType(BankAccountType.CHECKING);
        employeeBankAccount.setIsActive(true);

        bankAccountRepository.save(employeeBankAccount);
    }

    @Test
    void getBankAccountByIban_whenUserRequestsOwnAccount_returnBankAccount() throws Exception {

        mockMvc.perform(get(String.format("/bank-accounts/%s", userBankAccount.getIban()))
                .header("Authorization", "Bearer " + atmUserAuthToken))
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.iban").value(userBankAccount.getIban()));
    }

    @Test
    void getBankAccountByIban_whenEmployeeRequestsOwnAccount_returnBankAccount() throws Exception {

        mockMvc.perform(get(String.format("/bank-accounts/%s", employeeBankAccount.getIban()))
                .header("Authorization", "Bearer " + atmEmployeeAuthToken))
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.iban").value(employeeBankAccount.getIban()));
    }

    @Test
    void getBankAccountByIban_whenEmployeeRequestsUserAccount_returnBankAccount() throws Exception {

        mockMvc.perform(get(String.format("/bank-accounts/%s", userBankAccount.getIban()))
                .header("Authorization", "Bearer " + atmEmployeeAuthToken))
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.iban").value(userBankAccount.getIban()));
    }

    @Test
    void getBankAccountByIban_whenUserRequestsOtherUserAccount_returnNotFound() throws Exception {

        mockMvc.perform(get(String.format("/bank-accounts/%s", employeeBankAccount.getIban()))
                .header("Authorization", "Bearer " + atmUserAuthToken))
            .andExpect(status().is(404));
    }

    @Test
    void getBankAccountByIban_whenNotAuthenticated_returnUnauthorized() throws Exception {

        mockMvc.perform(get(String.format("/bank-accounts/%s", userBankAccount.getIban())))
            .andExpect(status().is(401));
    }

    @Test
    void getBankAccountByIban_whenNonExistentIban_returnNotFound() throws Exception {

        mockMvc.perform(get("/bank-accounts/NONEXISTENT")
                .header("Authorization", "Bearer " + atmUserAuthToken))
            .andExpect(status().is(404));
    }
}