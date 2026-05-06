package MoneyMachine.services;

import org.springframework.stereotype.Service;

@Service
public class UserService {
    public List<User> getAllUsersWithoutAnAccount()
    {
        List<User> users = new ArrayList<User>();
        return users;
    }
}
