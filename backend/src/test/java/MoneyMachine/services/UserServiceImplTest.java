package MoneyMachine.services;

import org.junit.jupiter.api.BeforeEach;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import MoneyMachine.models.User;
import MoneyMachine.services.interfaces.AuthenticationService;
import MoneyMachine.models.enums.Role;
import MoneyMachine.repositories.UserRepository;
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;
    private AuthenticationService authenticationService;
    @BeforeEach
    void setUp()
    {
        user = new User();
        user.setFirstName("employeeFirstName");
        user.setLastName("employeeLastName");
        user.setEmail("employee@employee.employee");
        user.setBsn("123456749");
        user.setPhoneNumber("+31 6 12 34 54 78");
        user.setRole(Role.EMPLOYEE);
        user.setPassword(authenticationService.getHashedPassword("password"));
        user.setIsActive(false);
        user.setIsApproved(false);
    }

    public void getAllUsersWithoutBankAccounts_whenUsersWithoutBankAccountsFound_returnAllUsers()
    {
        Iterable<User> users = userRepository.findByBankAccountsIsEmpty();
    }

    public void testApproveUserAndCreateAccounts()
    {

    }
}
