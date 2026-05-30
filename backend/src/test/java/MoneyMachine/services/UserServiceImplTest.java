package MoneyMachine.services;

import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import MoneyMachine.mappers.UserMapper;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.interfaces.UserService;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @InjectMocks
    private UserService userService;

    @Test
    void getAllUsersWithoutABankAccount()
    {
        when(userRepository.findByBankAccountsIsEmpty());
    }
}
