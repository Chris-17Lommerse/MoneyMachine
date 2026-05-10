package MoneyMachine.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

import MoneyMachine.models.User;
import MoneyMachine.models.dtos.ErrorDTO;
import MoneyMachine.models.enums.LoginType;
import MoneyMachine.models.exceptions.ExpiredException;
import MoneyMachine.models.exceptions.NotAuthorizedException;
import MoneyMachine.models.exceptions.SignatureInvalidException;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.Interfaces.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.mindrot.jbcrypt.BCrypt;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private UserRepository userRepository;
    private String AuthTokenSecretKey;
    private double authTokenExpirationInHours;
    private final String[] requiredClaims = {"role", "email", "firstName", "lastName"};

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
    public String generateAuthTokenFromUser(User user) {

        Algorithm algorithm = Algorithm.HMAC256(this.AuthTokenSecretKey);
        
        return JWT.create()
            .withSubject(user.getId().toString())
            .withClaim("role", user.getRole().toString())
            .withClaim("email", user.getEmail())
            .withClaim("firstName", user.getFirstName())
            .withClaim("lastName", user.getLastName())
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
    public void validateDecodedAuthToken(DecodedJWT decodedAuthToken) {

        if (decodedAuthToken.getSubject() == null) {
            throw new NotAuthorizedException("Token is missing subject.");
        }

        if (decodedAuthToken.getExpiresAt() == null) {
            throw new NotAuthorizedException("Token is missing expiration.");
        }

        if (decodedAuthToken.getExpiresAt().before(new Date())) {
            throw new ExpiredException("Token has expired.");
        }

        for (String requiredClaim : requiredClaims) {
            if (isClaimNullOrEmpty(decodedAuthToken.getClaim(requiredClaim))) {
                throw new NotAuthorizedException(String.format("Token is missing %s claim.", requiredClaim));
            }
        }
    }

    private boolean isClaimNullOrEmpty(Claim claim) {
        return claim == null || claim.isNull() || claim.isMissing() || claim.asString() == null || claim.asString().isBlank();
    }

    private User getLoggedInUserByHeader(HttpServletRequest request, HttpServletResponse response, String headerName) throws Exception {
        try {
            String authHeader = request.getHeader(headerName);

            if (authHeader == null) {
                throw new NotAuthorizedException(headerName + " header is required.");
            }

            String[] headerParts = authHeader.split(" ");

            if (headerParts.length != 2 || !headerParts[0].equalsIgnoreCase("bearer")) {
                throw new NotAuthorizedException("Invalid authorization header format.");
            }

            String authToken = headerParts[1];
            DecodedJWT decodedAuthToken = getDecodedAuthToken(authToken);
            validateDecodedAuthToken(decodedAuthToken);
            
            User user = this.userRepository.findById(Integer.parseInt(decodedAuthToken.getSubject()));
            
            if (user == null) {
                throw new NotAuthorizedException("User in your token does not exist.");
            }

            return user;
        } 
        catch (ExpiredException | NotAuthorizedException | JWTDecodeException ex) {
            authError(ex, response);
        } 
        
        return null;
    }

    public User getLoggedInAtmUser(HttpServletRequest request, HttpServletResponse response) throws Exception {
        return getLoggedInUserByHeader(request, response, "Authorization");
    }

    private ErrorDTO authError(Exception ex, HttpServletResponse response) throws Exception {
        response.setHeader("x-atm-auth-error", "invalid_token");
        throw ex;
    }
}
