package MoneyMachine.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

import MoneyMachine.exception.InvalidAuthTokenException;
import MoneyMachine.exception.NotAuthorizedException;
import MoneyMachine.models.User;
import MoneyMachine.models.enums.LoginType;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.interfaces.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private UserRepository userRepository;
    private String AuthTokenSecretKey;
    private double authTokenExpirationInHours;
    private final String[] requiredClaims = {"role", "email", "firstName", "lastName", "loginType"};

    public AuthenticationServiceImpl(UserRepository userRepository, @Value("${AUTH_TOKEN_SECRET_KEY}") String authTokenSecretKey, @Value("${AUTH_TOKEN_EXPIRATION_IN_HOURS}") double authTokenExpirationInHours) {
        this.userRepository = userRepository;
        this.AuthTokenSecretKey = authTokenSecretKey;
        this.authTokenExpirationInHours = authTokenExpirationInHours;
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) {
        
        User user = userRepository.findByEmail(email);

        if (user != null && BCrypt.checkpw(password, user.getPassword())) {
            return user;
        }

        return null;
    }

    @Override
    public String generateAuthTokenFromUserAndLoginType(User user, LoginType loginType) {

        Algorithm algorithm = Algorithm.HMAC256(this.AuthTokenSecretKey);
        
        return JWT.create()
            .withSubject(user.getId().toString())
            .withClaim("role", user.getRole().toString())
            .withClaim("email", user.getEmail())
            .withClaim("firstName", user.getFirstName())
            .withClaim("lastName", user.getLastName())
            .withClaim("loginType", loginType.toString())
            .withExpiresAt(new Date(System.currentTimeMillis() + (long)(3600000 * authTokenExpirationInHours)))
            .sign(algorithm);
    }

    @Override
    public String getHashedPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(10));
    }

    @Override
    public DecodedJWT getDecodedAuthToken(String authToken) {
        return JWT.decode(authToken);
    }

    @Override
    public void validateDecodedAuthToken(DecodedJWT decodedAuthToken, LoginType loginType) {

        if (decodedAuthToken.getSubject() == null) {
            throw new InvalidAuthTokenException("Token is missing subject.");
        }

        if (decodedAuthToken.getExpiresAt() == null) {
            throw new InvalidAuthTokenException("Token is missing expiration.");
        }

        if (decodedAuthToken.getExpiresAt().before(new Date())) {
            throw new InvalidAuthTokenException("Token has expired.");
        }

        for (String requiredClaim : requiredClaims) {
            if (isClaimNullOrEmpty(decodedAuthToken.getClaim(requiredClaim))) {
                throw new InvalidAuthTokenException(String.format("Token is missing %s claim.", requiredClaim));
            }
        }

        if (LoginType.valueOf(decodedAuthToken.getClaim("loginType").toString().replace("\"", "")) != loginType){
            throw new NotAuthorizedException("You are not logged in into this part of the application.");
        }
    }

    private boolean isClaimNullOrEmpty(Claim claim) {
        return claim == null || claim.isNull() || claim.isMissing() || claim.asString() == null || claim.asString().isBlank();
    }

    public User getLoggedInUserByLoginType(HttpServletRequest request, HttpServletResponse response, LoginType loginType) throws Exception {
        
        String authHeader = request.getHeader("Authorization");

        if (authHeader == null) {
            throw new NotAuthorizedException("Authorization header is required.");
        }

        String[] headerParts = authHeader.split(" ");

        if (headerParts.length != 2 || !headerParts[0].equalsIgnoreCase("bearer")) {
            throw new NotAuthorizedException("Invalid authorization header format.");
        }

        String authToken = headerParts[1];
        DecodedJWT decodedAuthToken = getDecodedAuthToken(authToken);
        validateDecodedAuthToken(decodedAuthToken, loginType);
        
        User user = this.userRepository.findById(Integer.parseInt(decodedAuthToken.getSubject()));
        
        if (user == null) {
            throw new InvalidAuthTokenException("User in your token does not exist.");
        }

        return user;
    }
}
