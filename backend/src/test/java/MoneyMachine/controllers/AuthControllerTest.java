package MoneyMachine.controllers;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

public class AuthControllerTest extends BaseControllerTest {

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

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.authToken").exists());
    }

    @Test
    void invalidLogin_whenInvalidPassword_returnInvalidCredentials() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("email", "user@user.user");
        request.put("password", "invalidPassword");
        request.put("loginType", "ATM");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().is(401));
    }
}
