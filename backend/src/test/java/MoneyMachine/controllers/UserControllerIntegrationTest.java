package MoneyMachine.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import jakarta.transaction.Transactional;
import com.fasterxml.jackson.databind.ObjectMapper;

import MoneyMachine.repositories.UserRepository;
@SpringBootTest
@Transactional
public class UserControllerIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired 
    private ObjectMapper objectMapper;
    @Autowired
    private UserRepository userRepository;

    @Test
    void getAllUsersWithoutABankAccount()
    {
        
    }
}