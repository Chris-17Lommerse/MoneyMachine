package MoneyMachine.services;

import static org.mockito.Mockito.when;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import MoneyMachine.mappers.UserMapper;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.models.User;
import MoneyMachine.models.enums.Role;
import MoneyMachine.models.dtos.responses.UserResponse;
import java.util.List;
@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;
    private UserResponse userResponse;
    private Iterable<User> users;
    private List<UserResponse> convertedUsers;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(Long.valueOf(1));
        user.setFirstName("employeeFirstName");
        user.setLastName("employeeLastName");
        user.setEmail("employee@employee.employee");
        user.setBsn("123456749");
        user.setPhoneNumber("+31 6 12 34 54 78");
        user.setRole(Role.EMPLOYEE);
        user.setIsApproved(false);
        user.setIsActive(false);
    }
    @Test
    void getAllUsersWithoutABankAccount()
    {
        when(userRepository.findByBankAccountsIsEmpty()).thenReturn(users);

        convertedUsers.add(userResponse);

        convertedUsers = userService.getAllUsersWithoutBankAccounts();
    }
}
