package MoneyMachine.services.Interfaces;

import com.auth0.jwt.interfaces.DecodedJWT;

import MoneyMachine.models.User;

public interface AuthenticationService {
    public User getUserByEmailAndPassword(String username, String password);
    public String generateTokenFromUser(User user);
    public DecodedJWT getDecodedToken(String token);
    public String getHashedPassword(String rawPassword);
}
