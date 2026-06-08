package MoneyMachine.controllers;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springdoc.core.converters.models.Pageable;

import MoneyMachine.models.BankAccount;
import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.requests.PatchRequest;
import MoneyMachine.models.enums.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import MoneyMachine.repositories.BankAccountRepository;

public class BankAccountControllerTest extends BaseControllerTest {

    @Autowired
    private BankAccountRepository bankAccountRepository;

    private BankAccount userBankAccount;
    private BankAccount employeeBankAccount;
    private Pageable pageable;
    private PatchRequest patchRequest;

    @BeforeEach
    void setUp() {
        super.setUpMockAuth();

        user = new User();
        user.setId(1l);
        user.setFirstName("employeeFirstName");
        user.setLastName("employeeLastName");
        user.setEmail("employee@employee.employee");
        user.setBsn("123456749");
        user.setPhoneNumber("+31 6 12 34 54 78");
        user.setRole(Role.EMPLOYEE);
        user.setPassword("MockedPassword");
        user.setIsActive(false);
        user.setIsApproved(false);

        userBankAccount = new BankAccount();
        userBankAccount.setIban("NL91ABNA0417164300");
        userBankAccount.setUser(user);
        userBankAccount.setBalance(new BigDecimal("1000.00"));
        userBankAccount.setAbsoluteLimit(new BigDecimal("0.00"));
        userBankAccount.setSingleTransferLimit(new BigDecimal("5000.00"));
        userBankAccount.setDailyTransferLimit(new BigDecimal("20000.00"));
        userBankAccount.setBankAccountType(BankAccountType.CHECKING);
        userBankAccount.setIsActive(true);

        bankAccountRepository.save(userBankAccount);

        employeeBankAccount = new BankAccount();
        employeeBankAccount.setIban("NL47ABNA0428395174");
        employeeBankAccount.setUser(employee);
        employeeBankAccount.setBalance(new BigDecimal("1000.00"));
        employeeBankAccount.setAbsoluteLimit(new BigDecimal("0.00"));
        employeeBankAccount.setSingleTransferLimit(new BigDecimal("5000.00"));
        employeeBankAccount.setDailyTransferLimit(new BigDecimal("20000.00"));
        employeeBankAccount.setBankAccountType(BankAccountType.CHECKING);
        employeeBankAccount.setIsActive(true);

        bankAccountRepository.save(employeeBankAccount);

        patchRequest = new PatchRequest();
        patchRequest.setActive(false);
    }

    @Test
    void getBankAccountByIban_whenUserRequestsOwnAccount_returnBankAccount() throws Exception {

        mockMvc.perform(get(String.format("/bank-accounts/%s", userBankAccount.getIban()))
                .header("Authorization", "Bearer " + atmUserAuthToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.iban").value(userBankAccount.getIban()));
    }

    @Test
    void getBankAccountByIban_whenEmployeeRequestsOwnAccount_returnBankAccount() throws Exception {

        mockMvc.perform(get(String.format("/bank-accounts/%s", employeeBankAccount.getIban()))
                .header("Authorization", "Bearer " + atmEmployeeAuthToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.iban").value(employeeBankAccount.getIban()));
    }

    @Test
    void getBankAccountByIban_whenEmployeeRequestsUserAccount_returnBankAccount() throws Exception {

        mockMvc.perform(get(String.format("/bank-accounts/%s", userBankAccount.getIban()))
                .header("Authorization", "Bearer " + atmEmployeeAuthToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.iban").value(userBankAccount.getIban()));
    }

    @Test
    void getBankAccountByIban_whenUserRequestsOtherUserAccount_returnNotFound() throws Exception {

        mockMvc.perform(get(String.format("/bank-accounts/%s", employeeBankAccount.getIban()))
                .header("Authorization", "Bearer " + atmUserAuthToken))
                .andExpect(status().is(404));
    }

    @Test
    void getOtherUserBankAccountByIban_whenAuthorized_getOtherUserBankAccount() throws Exception {
        mockMvc.perform(get(String.format("/bank-accounts/%s", userBankAccount.getIban()))
                .header("Authorization", "Bearer " + atmEmployeeAuthToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.iban").value(userBankAccount.getIban()));
    }

    @Test
    void getBankAccountByIban_whenNotAuthenticated_returnUnauthorized() throws Exception {

        mockMvc.perform(get(String.format("/bank-accounts/%s", userBankAccount.getIban())))
            .andExpect(status().is(401));
    }

    @Test
    void getBankAccountByIban_whenNonExistentIban_returnNotFound() throws Exception {

        mockMvc.perform(get("/bank-accounts/NONEXISTENT")
                .header("Authorization", "Bearer " + atmUserAuthToken))
            .andExpect(status().is(404));
    }

    @Test
    void getAllBankAccounts_whenAuthorized_getAllBankAccounts() throws Exception {
        mockMvc.perform(get(String.format("/bank-accounts", pageable))
                .header("Authorization", "Bearer " + websiteEmployeeAuthToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.items").exists())
                .andExpect(jsonPath("$.items[0].iban").value(userBankAccount.getIban()))
                .andExpect(jsonPath("$.items[0].userId").value(userBankAccount.getUser().getId()))
                .andExpect(jsonPath("$.items[0].bankAccountType").value(userBankAccount.getBankAccountType().name()))
                .andExpect(jsonPath("$.items[0].balance").value(userBankAccount.getBalance().doubleValue()))
                .andExpect(jsonPath("$.items[0].singleTransferLimit")
                        .value(userBankAccount.getSingleTransferLimit().doubleValue()))
                .andExpect(jsonPath("$.items[0].dailyTransferLimit")
                        .value(userBankAccount.getDailyTransferLimit().doubleValue()))
                .andExpect(jsonPath("$.items[0].absoluteLimit").value(userBankAccount.getAbsoluteLimit().doubleValue()))
                .andExpect(jsonPath("$.items[0].isActive").value(userBankAccount.getIsActive()));
    }

    @Test
    void failedGetAllBankAccounts_WhenNotAuthorized_GetUnAuthorizedError() throws Exception {
        mockMvc.perform(get(String.format("/bank-accounts", pageable))
                .header("Authorization", "Bearer " + websiteUserAuthToken))
                .andExpect(status().is(403));
    }

    @Test
    void closeBankAccount_whenBankAccountIsClosed_setIsActiveFalseAndReturnUpdatedBankAccount() throws Exception {
        mockMvc.perform(patch(String.format("/bank-accounts/%s", userBankAccount.getIban()))
                .content(objectMapper.writeValueAsString(patchRequest))
                .contentType(MediaType.APPLICATION_JSON)
                .header("Authorization", "Bearer " + websiteEmployeeAuthToken))
                .andExpect(status().is(200))
                .andExpect(jsonPath("$.isActive").value(patchRequest.isActive()));
    }

}
