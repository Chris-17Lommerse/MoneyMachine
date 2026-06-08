package MoneyMachine.policies;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import MoneyMachine.exception.NotAuthorizedException;
import MoneyMachine.exception.NotFoundException;
import MoneyMachine.models.User;
import MoneyMachine.models.enums.Role;

public class ApprovingPolicyTest {
    private User customer;
    private User employee;
    private ApprovingPolicy approvingPolicy;

    @BeforeEach
    void setUp()
    {
        approvingPolicy = new ApprovingPolicy();
        customer = new User();
        customer.setFirstName("customerFirstName");
        customer.setLastName("customerLastName");
        customer.setEmail("customer@customer.customer");
        customer.setBsn("123456789");
        customer.setPhoneNumber("+31 6 87 65 43 21");
        customer.setRole(Role.USER);
        customer.setPassword("password");
        customer.setIsApproved(false);
        customer.setIsActive(true);

        employee = new User();
        employee.setFirstName("employeeFirstName");
        employee.setLastName("employeeLastName");
        employee.setEmail("employee@employee.employee");
        employee.setBsn("987654321");
        employee.setPhoneNumber("+31 6 87 65 43 21");
        employee.setRole(Role.EMPLOYEE);
        employee.setPassword("password");
        employee.setIsActive(false);
        employee.setIsApproved(false);
    }

    @Test
    void enforceUserIsNotNull_UserIsFounnd() {
        assertDoesNotThrow(() -> approvingPolicy.enforceUserIsNotNull(customer));
    }

    @Test
    void enforceUserIsNotNull_UserIsNotFound() {
        assertThrows(NotFoundException.class, () -> approvingPolicy.enforceUserIsNotNull(null));
    }

    @Test
    void enforceUserIsNotActive_UserIsActive() {
        assertDoesNotThrow(() -> approvingPolicy.enforceUserIsNotActive(customer));
    }

    @Test
    void enforceUserIsNotActive_UserIsNotActive() {
        assertThrows(NotAuthorizedException.class, () -> approvingPolicy.enforceUserIsNotActive(employee));
    }

    @Test
    void enforceUserIsNotAuthorizedToCreateAccount_RoleIsEmployee()
    {
        assertThrows(NotAuthorizedException.class, () -> approvingPolicy.enforceUserIsNotAuthorizedToCreateAccount(employee));
    }

    @Test
    void enforceUserIsNotAuthorizedToCreateAccount_RoleIsUser() {
        assertDoesNotThrow(() -> approvingPolicy.enforceUserIsNotAuthorizedToCreateAccount(customer));
    }
}
