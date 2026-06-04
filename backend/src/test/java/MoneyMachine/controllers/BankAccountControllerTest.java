package MoneyMachine.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.converters.models.Pageable;

import MoneyMachine.models.BankAccount;

public class BankAccountControllerTest extends BaseControllerTest {

    private String userBankAccountIban = "NL91ABNA0417164300";
    private Pageable pageable;
    private boolean isActive = false;
    private BankAccount bankAccount;

    @BeforeEach
    void setUp() {
        super.setUpMockAuth();
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
     .andExpect(jsonPath("$.iban").value(bankAccount.getIban()))
     .andExpect(jsonPath("$.userId").value(bankAccount.getUser().getId()))
     .andExpect(jsonPath("$.bankAccountType").value(bankAccount.getBankAccountType()))
     .andExpect(jsonPath("$.balance").value(bankAccount.getBalance()))
     .andExpect(jsonPath("$.singleTransferLimit").value(bankAccount.getSingleTransferLimit()))
     .andExpect(jsonPath("$.dailyTransferLimit").value(bankAccount.getDailyTransferLimit()))
     .andExpect(jsonPath("$.absoluteLimit").value(bankAccount.getAbsoluteLimit()))
     .andExpect(jsonPath("$.isActive").value(bankAccount.getIsActive()));
    }

    @Test
    void closeBankAccount_whenBankAccountIsClosed_setIsActiveFalseAndReturnUpdatedBankAccount() throws Exception {
        mockMvc.perform(patch(String.format("/bank-accounts/{iban}", userBankAccountIban))
        .header("Authorization", "Bearer" + websiteEmployeeAuthToken))
    .andExpect(status().is(200))
    .andExpect(jsonPath("$.isActive").value(isActive));

    }


}
