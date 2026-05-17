package MoneyMachine.services.interfaces;

import com.auth0.jwt.interfaces.DecodedJWT;

import MoneyMachine.models.User;
import MoneyMachine.models.enums.LoginType;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

public interface AuthenticationService {
    public User getUserByEmailAndPassword(String username, String password);
    public String generateAuthTokenFromUserAndLoginType(User user, LoginType loginType);
    public DecodedJWT getDecodedAuthToken(String authToken);
    public String getHashedPassword(String rawPassword);
    public void validateDecodedAuthToken(DecodedJWT decodedAuthToken, LoginType loginType);
    public User getLoggedInUserByLoginType(HttpServletRequest request, HttpServletResponse response, LoginType loginType) throws Exception;
}
