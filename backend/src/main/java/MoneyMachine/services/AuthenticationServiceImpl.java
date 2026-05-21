package MoneyMachine.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import MoneyMachine.models.User;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.interfaces.AuthenticationService;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthenticationServiceImpl(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
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

    @Override
    public User getLoggedInUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }
}
