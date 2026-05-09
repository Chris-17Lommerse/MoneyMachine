package MoneyMachine.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.util.Date;

import MoneyMachine.models.User;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.Interfaces.AuthenticationService;
import org.mindrot.jbcrypt.BCrypt;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private UserRepository userRepository;
    private String tokenSecretKey;
    private double tokenExpirationInHours;

    public AuthenticationServiceImpl(UserRepository userRepository, @Value("${TOKEN_SECRET_KEY}") String tokenSecretKey, @Value("${TOKEN_EXPIRATION_IN_HOURS}") double tokenExpirationInHours) {
        this.userRepository = userRepository;
        this.tokenSecretKey = tokenSecretKey;
        this.tokenExpirationInHours = tokenExpirationInHours;
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
    public String generateTokenFromUser(User user) {

        Algorithm algorithm = Algorithm.HMAC256(this.tokenSecretKey);
        
        return JWT.create()
            .withSubject(user.getId().toString())
            .withClaim("role", user.getRole().toString())
            .withClaim("email", user.getEmail())
            .withClaim("firstName", user.getFirstName())
            .withClaim("lastName", user.getLastName())
            .withExpiresAt(new Date(System.currentTimeMillis() + (long)(3600000 * tokenExpirationInHours)))
            .sign(algorithm);
    }

    @Override
    public String getHashedPassword(String rawPassword) {
        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(10));
    }

    @Override
    public DecodedJWT getDecodedToken(String token) {
        
        DecodedJWT decoded = JWT.decode(token);

        System.out.println("=== JWT Claims ===");
        System.out.println("Subject    : " + decoded.getSubject());
        System.out.println("Expires    : " + decoded.getExpiresAt());
        System.out.println("Issued at  : " + decoded.getIssuedAt());
        System.out.println("Role       : " + decoded.getClaim("role").asString());
        System.out.println("Email      : " + decoded.getClaim("email").asString());
        System.out.println("First name : " + decoded.getClaim("firstName").asString());
        System.out.println("Last name  : " + decoded.getClaim("lastName").asString());

        return decoded;
    }
}
