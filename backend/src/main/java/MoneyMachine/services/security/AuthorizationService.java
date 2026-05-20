package MoneyMachine.services.security;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import MoneyMachine.models.User;
import MoneyMachine.models.enums.Role;
import MoneyMachine.services.interfaces.AuthenticationService;
import io.jsonwebtoken.Claims;

@Service
public class AuthorizationService {

    private final AuthenticationService authenticationService;

    public AuthorizationService(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    public boolean isLoggedIntoLoginType(String loginType) {

        Claims claims = (Claims) SecurityContextHolder.getContext().getAuthentication().getDetails();
        String decodedAuthTokenLoginType = claims.get("loginType", String.class);

        if (loginType.equals(decodedAuthTokenLoginType)) {
            return true;
        }

        return false;
    }

    public boolean isAllowedToGetUserById(Long id) {

        User user = authenticationService.getLoggedInUser();

        if (user.getRole() != Role.EMPLOYEE && id != user.getId()) {
            return false;
        }

        return true;
    }
}
