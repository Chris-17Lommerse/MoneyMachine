package MoneyMachine.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.converters.models.Pageable;

import MoneyMachine.models.BankAccount;
import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.services.AuthenticationServiceImpl;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.responses.ErrorResponse;
import MoneyMachine.models.enums.Role;

public class BankAccountControllerTest extends BaseControllerTest {

    private String userBankAccountIban = "NL91ABNA0417164300";
    private Pageable pageable;
    private boolean isActive = false;
    private BankAccount bankAccount;
    private User user;
    private AuthenticationServiceImpl authenticationServiceImpl;
    private ErrorResponse errorResponse;

    @BeforeEach
    void setUp() {
        super.setUpMockAuth();
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

    }

    @Test
    void getBankAccountByIban_whenAuthorized_getOwnOwnedBankAccount() throws Exception {
        mockMvc.perform(get(String.format("/bank-accounts/%s", userBankAccountIban))
                .header("Authorization", "Bearer " + atmUserAuthToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.iban").value(userBankAccountIban));
    }

    @Test
    void failGetBankAccountByIban_whenNoBankAccountExists_getError() throws Exception {
        mockMvc.perform(get(String.format("/bank-accounts/%s", "NonExistant"))
                .header("Authorization", "Bearer " + atmUserAuthToken))
                .andExpect(status().is(404));
    }

    @Test
    void getOtherUserBankAccountByIban_whenAuthorized_getOtherUserBankAccount() throws Exception {
        mockMvc.perform(get(String.format("/bank-accounts/%s", userBankAccountIban))
                .header("Authorization", "Bearer " + atmEmployeeAuthToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.iban").value(userBankAccountIban));
    }

    @Test
    void getAllBankAccounts_whenAuthorized_getAllBankAccounts() throws Exception {
        mockMvc.perform(get(String.format("/bank-accounts", pageable))
                .header("Authorization", "Bearer" + websiteEmployeeAuthToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.items").exists())
                .andExpect(jsonPath("$.[items].iban").value(bankAccount.getIban()))
                .andExpect(jsonPath("$.[items].userId").value(bankAccount.getUser().getId()))
                .andExpect(jsonPath("$.[items].bankAccountType").value(bankAccount.getBankAccountType()))
                .andExpect(jsonPath("$.[items].balance").value(bankAccount.getBalance()))
                .andExpect(jsonPath("$.[items].singleTransferLimit").value(bankAccount.getSingleTransferLimit()))
                .andExpect(jsonPath("$.[items].dailyTransferLimit").value(bankAccount.getDailyTransferLimit()))
                .andExpect(jsonPath("$.[items].absoluteLimit").value(bankAccount.getAbsoluteLimit()))
                .andExpect(jsonPath("$.[items].isActive").value(bankAccount.getIsActive()));
    }


    @Test
    void failedGetAllBankAccounts_WhenNotAuthorized_GetUnAuthorizedError() throws Exception {
        mockMvc.perform(get(String.format("/bank-accounts", pageable))
        .header("Authorization", "Bearer" + websiteEmployeeAuthToken))
        .andExpect(status().is(401))
        .andExpect(jsonPath("$.code").value(errorResponse.getCode()))
        .andExpect(jsonPath("$.type").value(errorResponse.getErrorType()))
        .andExpect(jsonPath("$.message").value(errorResponse.getMessage()));
    }

    @Test
    void closeBankAccount_whenBankAccountIsClosed_setIsActiveFalseAndReturnUpdatedBankAccount() throws Exception {
        mockMvc.perform(patch(String.format("/bank-accounts/{iban}", userBankAccountIban))
                .header("Authorization", "Bearer" + websiteEmployeeAuthToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.isActive").value(isActive));
    }

}
