package MoneyMachine.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;
import MoneyMachine.models.User;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.Interfaces.UserService;

@Service
public class UserServiceImpl implements UserService {

    public List<User> getAllUsersWithoutAnAccount()
    {
        List<User> users = new  ArrayList<User>();
        return users;
    }
}
