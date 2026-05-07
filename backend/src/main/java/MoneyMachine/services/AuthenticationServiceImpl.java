package MoneyMachine.services;

import org.springframework.stereotype.Service;

import MoneyMachine.models.User;
import MoneyMachine.services.Interfaces.AuthenticationService;
import org.mindrot.jbcrypt.BCrypt;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

    @Override
    public User getUserByUsernameAndPassword(String username, String password) {
        
        // User user = usersRepository.getFullyKnownUserByUsername(username);

        // if (user != null && encoder.matches(password, user.getPassword())) {
        //     return user;
        // }

        return null;
    }

    @Override
    public String generateTokenFromUser(User user) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'generateTokenFromUser'");
    }

    @Override
    public String getHashedPassword(String rawPassword) {

        return BCrypt.hashpw(rawPassword, BCrypt.gensalt(10));
    }
}
