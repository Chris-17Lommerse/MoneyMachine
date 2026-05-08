package MoneyMachine.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import java.util.Date;

import MoneyMachine.models.User;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.Interfaces.AuthenticationService;
import org.mindrot.jbcrypt.BCrypt;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private UserRepository userRepository;
    private String jwtSecret = "NEEDSTOBEINA_ENVFILENEEDSTOBEINA_ENVFILENEEDSTOBEINA_ENVFILENEEDSTOBEINA_ENVFILE";
    private double tokenExpirationInHours = 1;

    public AuthenticationServiceImpl(UserRepository userRepository){
        this.userRepository = userRepository;
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

        Algorithm algorithm = Algorithm.HMAC256(this.jwtSecret);
        
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
}
