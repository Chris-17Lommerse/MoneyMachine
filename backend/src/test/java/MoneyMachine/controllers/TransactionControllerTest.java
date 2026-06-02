package MoneyMachine.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import MoneyMachine.models.BankAccount;
import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.repositories.BankAccountRepository;

public class TransactionControllerTest extends BaseControllerTest {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private final String userBankAccountIban = "NL91ABNA0417164300";
    private BankAccount employeeBankAccount;

    @BeforeEach
    void setUp() {
        super.setUpMockAuth();

        employeeBankAccount = new BankAccount();
        employeeBankAccount.setIban("NL47ABNA0428395174");
        employeeBankAccount.setUser(employee);
        employeeBankAccount.setBalance(new BigDecimal("3434"));
        employeeBankAccount.setAbsoluteLimit(new BigDecimal("-1000"));
        employeeBankAccount.setSingleTransferLimit(new BigDecimal("1000"));
        employeeBankAccount.setDailyTransferLimit(new BigDecimal("1000"));
        employeeBankAccount.setBankAccountType(BankAccountType.CHECKING);
        employeeBankAccount.setIsActive(true);

        bankAccountRepository.save(employeeBankAccount);
    }

    @Test
    void deposit_whenAuthorized_depositAmount() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("amount", 10);
        request.put("toBankAcountIban", userBankAccountIban);

        mockMvc.perform(post("/transactions/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + atmUserAuthToken))
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.amount").value(10))
            .andExpect(jsonPath("$.toAccountIban").value(userBankAccountIban))
            .andExpect(jsonPath("$.initiatingUserId").value(user.getId()));
    }

    @Test
    void depositOnOtherUser_whenAuthorized_depositAmount() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("amount", 10);
        request.put("toBankAcountIban", userBankAccountIban);

        mockMvc.perform(post("/transactions/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + atmEmployeeAuthToken))
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.amount").value(10))
            .andExpect(jsonPath("$.toAccountIban").value(userBankAccountIban))
            .andExpect(jsonPath("$.initiatingUserId").value(employee.getId()));
    }

    @Test
    void failDepositMaxLimit_whenDepositAboveMaxLimit_displayError() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("amount", 999999999);
        request.put("toBankAcountIban", userBankAccountIban);

        mockMvc.perform(post("/transactions/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + atmUserAuthToken))
            .andExpect(status().is(400));
    }

    @Test
    void failDepositAuthorize_whenDepositOtherUserNotOtherized_displayError() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("amount", 100);
        request.put("toBankAcountIban", employeeBankAccount.getIban());

        mockMvc.perform(post("/transactions/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + atmUserAuthToken))
            .andExpect(status().is(401));
    }

    @Test
    void withdraw_whenAuthorized_withdrawAmount() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("amount", 10);
        request.put("fromBankAcountIban", userBankAccountIban);

        mockMvc.perform(post("/transactions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + atmUserAuthToken))
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.amount").value(10))
            .andExpect(jsonPath("$.fromAccountIban").value(userBankAccountIban))
            .andExpect(jsonPath("$.initiatingUserId").value(user.getId()));
    }

    @Test
    void withdrawFromOtherUser_whenAuthorized_withdrawAmount() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("amount", 10);
        request.put("fromBankAcountIban", userBankAccountIban);

        mockMvc.perform(post("/transactions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + atmEmployeeAuthToken))
            .andExpect(status().is(201))
            .andExpect(jsonPath("$.amount").value(10))
            .andExpect(jsonPath("$.fromAccountIban").value(userBankAccountIban))
            .andExpect(jsonPath("$.initiatingUserId").value(employee.getId()));
    }

    @Test
    void failWithdrawMaxLimit_whenWithdrawAboveMaxLimit_displayError() throws Exception {

        Map<String, Object> request = new HashMap<>();
        request.put("amount", 99999);
        request.put("fromBankAcountIban", userBankAccountIban);

        mockMvc.perform(post("/transactions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + atmUserAuthToken))
            .andExpect(status().is(400));
    }

    @Test
    void failWithdrawAbsoluteLimit_whenWithdrawUnderAbsoluteLimit_displayError() throws Exception {

        employeeBankAccount.setBalance(new BigDecimal("-1000"));
        bankAccountRepository.save(employeeBankAccount);

        Map<String, Object> request = new HashMap<>();
        request.put("amount", 100);
        request.put("fromBankAcountIban", employeeBankAccount.getIban());

        mockMvc.perform(post("/transactions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + atmEmployeeAuthToken))
            .andExpect(status().is(400));
    }

    @Test
    void failWithdrawAuthorize_whenWithdrawOtherUserNotOtherized_displayError() throws Exception {

         Map<String, Object> request = new HashMap<>();
        request.put("amount", 100);
        request.put("fromBankAcountIban", employeeBankAccount.getIban());

        mockMvc.perform(post("/transactions/withdraw")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request))
                .header("Authorization", "Bearer " + atmUserAuthToken))
            .andExpect(status().is(401));
    }
}
