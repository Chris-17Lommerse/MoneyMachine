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
import MoneyMachine.models.dtos.responses.UserOverviewResponse;
import MoneyMachine.models.dtos.responses.UserResponse;
import MoneyMachine.models.enums.Role;
import MoneyMachine.policies.ApprovingPolicy;
import MoneyMachine.repositories.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;
    
    @Mock
    private UserMapper userMapper;

    @Mock
    private Page<User> page;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;

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
        user.setPassword("MockedPassword");
        user.setIsActive(false);
        user.setIsApproved(false);
    }

    @Test
    public void getAllUsersWithoutBankAccounts_whenUsersWithoutBankAccountsFound_returnAllUsers(Pageable pageable)
    {
        List<User> users = page.getContent();
        when(userRepository.findByBankAccountsIsEmpty(pageable)).thenReturn(page);

        List<UserResponse> items = userMapper.toResponseList(users);
        UserOverviewResponse userOverviewResponse = new UserOverviewResponse(items, page.getNumber(), page.getSize());
        userOverviewResponse = userServiceImpl.getAllUsersWithoutBankAccounts(pageable);

        assertEquals(users, userOverviewResponse);

        verify(userRepository.findByBankAccountsIsEmpty(pageable));
    }
}
