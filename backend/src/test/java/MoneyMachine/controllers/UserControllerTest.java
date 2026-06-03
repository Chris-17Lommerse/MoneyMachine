package MoneyMachine.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class UserControllerTest extends BaseControllerTest {

    @BeforeEach
    void setUp() {
        super.setUpMockAuth();
    }

    @Test
    void getBankAccountsByUserId_whenGetCall_getBankAccounts() throws Exception {
        mockMvc.perform(get(String.format("/users/%s/bank-accounts", user.getId()))
                .header("Authorization", "Bearer " + atmUserAuthToken))
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.items").exists());
    }

    @Test
    void failGettingBankAccounts_whenNotAuthorized_getForbidden() throws Exception {
        mockMvc.perform(get(String.format("/users/%s/bank-accounts", employee.getId()))
                .header("Authorization", "Bearer " + atmUserAuthToken))
            .andExpect(status().is(403));
    }

    @Test
    void gettingBankAccountsOfOtherUser_whenAuthorized_getTheirBankAccounts() throws Exception {
        mockMvc.perform(get(String.format("/users/%s/bank-accounts", user.getId()))
                .header("Authorization", "Bearer " + atmEmployeeAuthToken))
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.items").exists());
    }
}
