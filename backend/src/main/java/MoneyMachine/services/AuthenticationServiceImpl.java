package MoneyMachine.services;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

import MoneyMachine.exception.InvalidAuthTokenException;
import MoneyMachine.exception.NotAuthorizedException;
import MoneyMachine.models.User;
import MoneyMachine.models.enums.LoginType;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.interfaces.AuthenticationService;
import MoneyMachine.util.JwtUtil;
import io.jsonwebtoken.Jwt;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public User getUserByEmailAndPassword(String email, String password) {
        
        User user = userRepository.findByEmail(email);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }

        return null;
    }

    @Override
    public String getHashedPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    // @Override
    // public void validateDecodedAuthToken(DecodedJWT decodedAuthToken, LoginType loginType) {

    //     if (decodedAuthToken.getSubject() == null) {
    //         throw new InvalidAuthTokenException("Token is missing subject.");
    //     }

    //     if (decodedAuthToken.getExpiresAt() == null) {
    //         throw new InvalidAuthTokenException("Token is missing expiration.");
    //     }

    //     if (decodedAuthToken.getExpiresAt().before(new Date())) {
    //         throw new InvalidAuthTokenException("Token has expired.");
    //     }

    //     for (String requiredClaim : requiredClaims) {
    //         if (isClaimNullOrEmpty(decodedAuthToken.getClaim(requiredClaim))) {
    //             throw new InvalidAuthTokenException(String.format("Token is missing %s claim.", requiredClaim));
    //         }
    //     }

    //     if (LoginType.valueOf(decodedAuthToken.getClaim("loginType").toString().replace("\"", "")) != loginType){
    //         throw new NotAuthorizedException("You are not logged in into this part of the application.");
    //     }
    // }
}
