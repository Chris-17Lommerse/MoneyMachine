package MoneyMachine.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import MoneyMachine.mappers.UserMapper;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.UserDTO;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper)
    {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public List<UserDTO> getAllUsersWithoutBankAccounts() {
        Iterable<User> users = userRepository.findByBankAccountsIsEmpty();
        List<UserDTO> convertedUsers = new ArrayList<UserDTO>();

        for (User user : users) {
            convertedUsers.add(userMapper.toDTO(user));
        }
        return convertedUsers;
    }
}
