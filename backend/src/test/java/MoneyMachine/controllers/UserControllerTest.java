package MoneyMachine.controllers;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import MoneyMachine.models.User;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest extends BaseControllerTest {


   private User user;

    @BeforeEach
    void setUp() {
        super.setUpMockAuth();
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
    .andExpect(jsonPath("$.userId").value(user.getId()))
    .andExpect(jsonPath("$.furstName").value(user.getFirstName()))
    .andExpect(jsonPath("$.lastName").value(user.getLastName()))
    .andExpect(jsonPath("$.email").value(user.getEmail()))
    .andExpect(jsonPath("$.bsn").value(user.getBsn()))
    .andExpect(jsonPath("$.phoneNumber").value(user.getPhoneNumber()))
    .andExpect(jsonPath("$.role").value(user.getRole()))
    .andExpect(jsonPath("$.isActive").value(user.getIsActive()))
    .andExpect(jsonPath("$.isApproved").value(user.getIsApproved()));
    }
}
