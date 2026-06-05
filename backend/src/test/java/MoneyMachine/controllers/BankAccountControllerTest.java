package MoneyMachine.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class BankAccountControllerTest extends BaseControllerTest {

    private String userBankAccountIban = "NL91ABNA0417164300";

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
}
