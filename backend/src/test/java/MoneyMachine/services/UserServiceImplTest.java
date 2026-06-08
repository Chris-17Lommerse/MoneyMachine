package MoneyMachine.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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

    @Mock
    private List<User> users;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;
    private User customer;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
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

        customer = new User();
        customer.setFirstName("customerFirstName");
        customer.setLastName("customerLastName");
        customer.setEmail("customer@customer.customer");
        customer.setBsn("987654321");
        customer.setPhoneNumber("+31 6 87 65 43 21");
        customer.setRole(Role.USER);
        customer.setPassword("MockedPassword");
        customer.setIsActive(false);
        customer.setIsApproved(false);
    }

    @Test
    public void getAllUsersWithoutBankAccounts_whenUsersWithoutBankAccountsFound_returnAllUsers() {
        users = List.of(customer, user);
        when(userRepository.findByBankAccountsIsEmpty(pageable)).thenReturn(page);
        when(page.getContent()).thenReturn(users);
        when(page.getNumber()).thenReturn(0);
        when(page.getSize()).thenReturn(2);

        List<UserResponse> expectedItems = List.of(
            new UserResponse(),
            new UserResponse()
        );

        when(userMapper.toResponseList(users)).thenReturn(expectedItems);

        UserOverviewResponse userOverviewResponse = userServiceImpl.getAllUsersWithoutBankAccounts(pageable);
        assertEquals(2, userOverviewResponse.getItems().size());
        assertEquals(0, userOverviewResponse.getPage());
        assertEquals(2, userOverviewResponse.getPageSize());
        assertNotNull(userOverviewResponse);

        verify(userRepository).findByBankAccountsIsEmpty(pageable);
        verify(userMapper).toResponseList(users);
    }
}
