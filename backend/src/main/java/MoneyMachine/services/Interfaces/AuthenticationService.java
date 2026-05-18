package MoneyMachine.services.interfaces;

import MoneyMachine.models.User;

public interface AuthenticationService {
    public User getUserByEmailAndPassword(String username, String password);
    public String getHashedPassword(String rawPassword);
}
