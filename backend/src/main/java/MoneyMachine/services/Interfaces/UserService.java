package MoneyMachine.services.Interfaces;

import java.util.List;

import MoneyMachine.models.User;

public interface UserService {
    public List<User> getAllUsersWithoutAnAccount();
}
