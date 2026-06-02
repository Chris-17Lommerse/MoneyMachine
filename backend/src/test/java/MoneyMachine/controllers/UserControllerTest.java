package MoneyMachine.controllers;

import java.util.HashMap;
import java.util.Map;

import MoneyMachine.models.enums.LoginType;
import MoneyMachine.models.enums.Role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import MoneyMachine.models.User;
import MoneyMachine.util.JwtUtil;

import org.springframework.http.MediaType;

import tools.jackson.databind.ObjectMapper;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private JwtUtil jwtUtil;

    private String validAtmUserToken;
    private String validAtmEmployeeToken;
    private User user;
    private User employee;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("userFirstName");
        user.setLastName("userLastName");
        user.setEmail("user@user.user");
        user.setRole(Role.USER);

        employee = new User();
        employee.setId(2L);
        employee.setFirstName("employeeFirstName");
        employee.setLastName("employeeLastName");
        employee.setEmail("employee@employee.employee");
        employee.setRole(Role.EMPLOYEE);
    
        validAtmUserToken = jwtUtil.generateAuthTokenFromUser(user, LoginType.ATM);
        validAtmEmployeeToken = jwtUtil.generateAuthTokenFromUser(employee, LoginType.ATM);
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
                .header("Authorization", "Bearer " + validAtmUserToken))
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.items").exists());
    }

    @Test
    void failGettingBankAccounts_whenNotAuthorized_getForbidden() throws Exception {
        mockMvc.perform(get(String.format("/users/%s/bank-accounts", employee.getId()))
                .header("Authorization", "Bearer " + validAtmUserToken))
            .andExpect(status().is(403));
    }

    @Test
    void gettingBankAccountsOfOtherUser_whenAuthorized_getTheirBankAccounts() throws Exception {
        mockMvc.perform(get(String.format("/users/%s/bank-accounts", user.getId()))
                .header("Authorization", "Bearer " + validAtmEmployeeToken))
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.items").exists());
    }
}
