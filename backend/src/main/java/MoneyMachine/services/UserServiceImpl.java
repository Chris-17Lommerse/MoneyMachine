package MoneyMachine.services;

import MoneyMachine.mappers.UserMapper;
import java.util.List;

import org.springframework.stereotype.Service;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.UserDTO;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.interfaces.UserService;
import java.util.ArrayList;

@Service
public class UserServiceJpa implements UserService {
    private UserMapper userMapper;
    private UserRepository userRepository;

    public UserServiceJpa(UserRepository userRepository, UserMapper userMapper)
    {
        this.userRepository = userRepository;
        this.userMapper = userMapper;
    }

    public List<UserDTO> getAllUsersWithoutBankAccounts() {
        Iterable<User> users = userRepository.findByBankAccountsIsEmpty();
        List<UserDTO> convertedUsers = new ArrayList<UserDTO>();

        for(User user : users)
        {
            convertedUsers.add(userMapper.toDTO(user));
        }
        return convertedUsers;
    }
}
