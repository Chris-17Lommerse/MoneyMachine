package MoneyMachine.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import MoneyMachine.mappers.UserMapper;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.responses.UserResponse;
import MoneyMachine.models.enums.Role;
import MoneyMachine.repositories.UserRepository;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;

    @InjectMocks
    private AuthenticationServiceImpl authenticationServiceImpl;
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
        user.setPassword(authenticationServiceImpl.getHashedPassword("password"));
        user.setIsActive(false);
        user.setIsApproved(false);
    }

    public void getAllUsersWithoutBankAccounts_whenUsersWithoutBankAccountsFound_returnAllUsers()
    {
        Iterable<User> iterableUsers = List.of(new User(), new User());
        when(userRepository.findByBankAccountsIsEmpty()).thenReturn(iterableUsers);

        List<UserResponse> userResponses = userServiceImpl.getAllUsersWithoutBankAccounts();
        assertEquals(userResponses, iterableUsers);

        verify(userRepository.findByBankAccountsIsEmpty());
    }

    public void testApproveUserAndCreateAccounts()
    {

    }
}
