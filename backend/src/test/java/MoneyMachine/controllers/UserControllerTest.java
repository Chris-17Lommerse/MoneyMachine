package MoneyMachine.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import MoneyMachine.models.User;
import MoneyMachine.services.AuthenticationServiceImpl;
import MoneyMachine.models.enums.Role;
import java.util.Map;
import java.util.HashMap;
import org.springframework.http.MediaType;

public class UserControllerTest extends BaseControllerTest {

    private User user;
    private AuthenticationServiceImpl authenticationServiceImpl;

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
    }

    @Test
    void login_whenLoginAsUser_getAuthenticationResponse() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("email", "user@user.user");
        request.put("password", "password");
        request.put("loginType", "ATM");

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(201))
                .andExpect(jsonPath("$.accessToken").exists())
                .andExpect(jsonPath("$.expiresIn").exists())
                .andExpect(jsonPath("$.userSummaryResponse").exists())
                .andExpect(jsonPath("$.userSummaryResponse.id").value(1));
    }

    @Test
    void invalidLogin_whenInvalidPassword_returnInvalidCredentials() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("email", "user@user.user");
        request.put("password", "invalidPassword");
        request.put("loginType", "ATM");

        mockMvc.perform(post("/users/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().is(401));
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

    @Test
    void getAllBankAccounts_whenAuthorized_getAllBankAccounts() throws Exception {
        mockMvc.perform(get(String.format("/users"))
                .header("Authorization", "Bearer" + websiteEmployeeAuthToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.items").exists())
                .andExpect(jsonPath("$.[items].userId").value(user.getId()))
                .andExpect(jsonPath("$.[items].firstName").value(user.getFirstName()))
                .andExpect(jsonPath("$.[items].lastName").value(user.getLastName()))
                .andExpect(jsonPath("$.[items].email").value(user.getEmail()))
                .andExpect(jsonPath("$.[items].bsn").value(user.getBsn()))
                .andExpect(jsonPath("$.[items].phoneNumber").value(user.getPhoneNumber()))
                .andExpect(jsonPath("$.[items].role").value(user.getRole()))
                .andExpect(jsonPath("$.[items].isActive").value(user.getIsActive()))
                .andExpect(jsonPath("$.[items].isApproved").value(user.getIsApproved()));
    }
}
