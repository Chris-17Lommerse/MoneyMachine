package MoneyMachine.controllers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class TransactionControllerTest extends BaseControllerTest {

    @BeforeEach
    void setUp() {
        super.setUpMockAuth();
    }

    @Test
    void deposit_whenAuthorized_depositAmount() {

    }

    @Test
    void depositOnOtherUser_whenAuthorized_depositAmount() {

    }

    @Test
    void failDepositMaxLimit_whenDepositAboveMaxLimit_displayError() {

    }

    @Test
    void failDepositAuthorize_whenDepositOtherUserNotOtherized_displayError() {

    }

    @Test
    void withdraw_whenAuthorized_withdrawAmount() {

    }

    @Test
    void withdrawFromOtherUser_whenAuthorized_withdrawAmount() {

    }

    @Test
    void failWithdrawMaxLimit_whenWithdrawAboveMaxLimit_displayError() {

    }

    @Test
    void failWithdrawAbsoluteLimit_whenWithdrawUnderAbsoluteLimit_displayError() {

    }

    @Test
    void failWithdrawAuthorize_whenWithdrawOtherUserNotOtherized_displayError() {

    }
}
