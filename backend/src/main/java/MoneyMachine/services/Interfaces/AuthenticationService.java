package MoneyMachine.services.Interfaces;

import MoneyMachine.models.User;

public interface AuthenticationService {
    public User getUserByUsernameAndPassword(String username, String password);
    public String generateTokenFromUser(User user);
    //public getDecodedToken(string $token): stdClass;
    public String getHashedPassword(String rawPassword);
}
