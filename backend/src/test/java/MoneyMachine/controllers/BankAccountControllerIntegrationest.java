package MoneyMachine.controllers;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;

import MoneyMachine.repositories.BankAccountRepository;
import jakarta.transaction.Transactional;

@SpringBootTest
@Transactional
public class BankAccountControllerIntegrationest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private BankAccountRepository BankAccountRepository;

    @Test
    void getAllBankAccounts() throws Exception {
        
    }

    @Test
    void setBankAccountInactive()
    {

    }
}
