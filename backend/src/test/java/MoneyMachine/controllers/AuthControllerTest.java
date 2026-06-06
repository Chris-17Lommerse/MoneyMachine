package MoneyMachine.controllers;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import org.springframework.http.MediaType;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
    void login_whenLoginAsEmployee_getAuthenticationResponse() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("email", "employee@employee.employee");
        request.put("password", "password");
        request.put("loginType", "WEBSITE");

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

    @Test
    void invalidLogin_whenInvalidEmail_returnInvalidCredentials() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("email", "nonexistent@user.user");
        request.put("password", "password");
        request.put("loginType", "ATM");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().is(401));
    }

    @Test
    void invalidLogin_whenMissingEmail_returnBadRequest() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("password", "password");
        request.put("loginType", "ATM");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().is(400));
    }

    @Test
    void invalidLogin_whenMissingPassword_returnBadRequest() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("email", "user@user.user");
        request.put("loginType", "ATM");

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().is(400));
    }

    @Test
    void invalidLogin_whenEmptyBody_returnBadRequest() throws Exception {

        mockMvc.perform(post("/auth/login")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{}"))
            .andExpect(status().is(400));
    }

    @Test
    void getLoggedInUser_whenAuthenticatedAsUser_returnUserResponse() throws Exception {

        mockMvc.perform(get("/auth/me")
                .header("Authorization", "Bearer " + atmUserAuthToken))
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.email").value(user.getEmail()));
    }

    @Test
    void getLoggedInUser_whenAuthenticatedAsEmployee_returnUserResponse() throws Exception {

        mockMvc.perform(get("/auth/me")
                .header("Authorization", "Bearer " + atmEmployeeAuthToken))
            .andExpect(status().is(200))
            .andExpect(jsonPath("$.email").value(employee.getEmail()));
    }

    @Test
    void getLoggedInUser_whenNotAuthenticated_returnUnauthorized() throws Exception {

        mockMvc.perform(get("/auth/me"))
            .andExpect(status().is(401));
    }

    @Test
    void getLoggedInUser_whenInvalidToken_returnUnauthorized() throws Exception {

        mockMvc.perform(get("/auth/me")
                .header("Authorization", "Bearer invalidToken"))
            .andExpect(status().is(401));
    }
}