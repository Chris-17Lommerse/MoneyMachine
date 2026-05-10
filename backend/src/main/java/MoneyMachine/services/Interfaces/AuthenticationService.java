package MoneyMachine.services.Interfaces;

import com.auth0.jwt.interfaces.DecodedJWT;

import MoneyMachine.models.User;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    public User getUserByEmailAndPassword(String username, String password);
    public String generateAuthTokenFromUser(User user);
    public DecodedJWT getDecodedAuthToken(String authToken);
    public String getHashedPassword(String rawPassword);
    public void validateDecodedAuthToken(DecodedJWT decodedAuthToken);
    public User getLoggedInAtmUser(HttpServletRequest request, HttpServletResponse response) throws Exception;
}
