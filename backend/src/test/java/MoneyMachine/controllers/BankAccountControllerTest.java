package MoneyMachine.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import MoneyMachine.models.User;
import MoneyMachine.models.enums.LoginType;
import MoneyMachine.models.enums.Role;
import MoneyMachine.util.JwtUtil;

public class BankAccountControllerTest {

    @Autowired
    private JwtUtil jwtUtil;

    private String validAtmToken;
    private User loggedInAtmUser;

    @BeforeEach
    void setUp() {
        loggedInAtmUser = new User();
        loggedInAtmUser.setId(1L);
        loggedInAtmUser.setFirstName("userFirstName");
        loggedInAtmUser.setLastName("userLastName");
        loggedInAtmUser.setEmail("user@user.user");
        loggedInAtmUser.setRole(Role.USER);
    
        validAtmToken = jwtUtil.generateAuthTokenFromUser(loggedInAtmUser, LoginType.ATM);
    }

    @Test
    void testGetBankAccountByIban() {

    }
}
