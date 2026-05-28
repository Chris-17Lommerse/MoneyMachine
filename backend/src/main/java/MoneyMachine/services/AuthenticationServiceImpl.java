package MoneyMachine.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import MoneyMachine.exception.InvalidCredentialsException;
import MoneyMachine.mappers.UserMapper;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.responses.LoginResponse;
import MoneyMachine.models.dtos.responses.UserResponse;
import MoneyMachine.models.enums.LoginType;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.interfaces.AuthenticationService;
import MoneyMachine.util.JwtUtil;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final UserMapper userMapper;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserMapper userMapper) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userMapper = userMapper;
    }

    private User getUserByEmailAndPassword(String email, String password) {
        
        User user = userRepository.findByEmail(email);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            return user;
        }

        throw new InvalidCredentialsException("Password or email is not correct.");
    }

    @Override
    public String getHashedPassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Override
    public User getLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    @Override
    public LoginResponse login(String email, String password, LoginType loginType) {

        User user = getUserByEmailAndPassword(email, password);
        String authToken = jwtUtil.generateAuthTokenFromUser(user, loginType);

        return new LoginResponse(authToken, "Bearer", jwtUtil.getAuthTokenExpirationTime(), userMapper.toSummaryResponse(user));
    }

    @Override
    public UserResponse getLoggedInUserResponse() {

        User user = getLoggedInUser();
        
        return userMapper.toResponse(user);
    }
}
