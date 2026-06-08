package MoneyMachine.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import MoneyMachine.models.User;
import MoneyMachine.models.enums.Role;

public class UserControllerTest extends BaseControllerTest {

    private User user;

    @BeforeEach
    void setUp() {
        super.setUpMockAuth();
        user = new User();
        user.setId(2l);
        user.setFirstName("employeeFirstName");
        user.setLastName("employeeLastName");
        user.setEmail("employee@employee.employee");
        user.setBsn("123456749");
        user.setPhoneNumber("+31 6 12 34 54 78");
        user.setRole(Role.EMPLOYEE);
        user.setPassword("MockedPassword");
        user.setIsActive(false);
        user.setIsApproved(false);
    }

    @Test
    void getBankAccountsByUserId_whenGetCall_getBankAccounts() throws Exception {
        mockMvc.perform(get(String.format("/users/%s/bank-accounts", user.getId()))
                .header("Authorization", "Bearer " + websiteEmployeeAuthToken))
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

    @Test
    void getAllBankAccounts_whenAuthorized_getAllBankAccounts() throws Exception {
        mockMvc.perform(get(String.format("/users"))
                .header("Authorization", "Bearer " + websiteEmployeeAuthToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items").exists())
                .andExpect(jsonPath("$.items[0].userId").value(user.getId()))
                .andExpect(jsonPath("$.items[0].firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.items[0].lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.items[0].email").value(user.getEmail()))
                .andExpect(jsonPath("$.items[0].bsn").value(user.getBsn()))
                .andExpect(jsonPath("$.items[0].phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.items[0].role").value(user.getRole().name()))
                .andExpect(jsonPath("$.items[0].isActive").value(user.getIsActive()))
                .andExpect(jsonPath("$.items[0].isApproved").value(user.getIsApproved()));
    }
}
