package MoneyMachine.services.interfaces;

import MoneyMachine.models.User;
import MoneyMachine.models.dtos.responses.LoginResponse;
import MoneyMachine.models.dtos.responses.UserResponse;
import MoneyMachine.models.enums.LoginType;

public interface AuthenticationService {
    public String getHashedPassword(String rawPassword);
    public User getLoggedInUser();
    public UserResponse getLoggedInUserResponse();
    public LoginResponse login(String email, String password, LoginType loginType);
}