package MoneyMachine.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.auth0.jwt.interfaces.DecodedJWT;

import MoneyMachine.models.User;
import MoneyMachine.models.dtos.ErrorDTO;
import MoneyMachine.models.enums.ErrorType;
import MoneyMachine.models.exceptions.ExpiredException;
import MoneyMachine.models.exceptions.NotAuthorizedException;
import MoneyMachine.models.exceptions.SignatureInvalidException;
import MoneyMachine.services.Interfaces.AuthenticationService;
import MoneyMachine.services.Interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.NoArgsConstructor;

@Controller
@CrossOrigin(origins = "*")
@NoArgsConstructor
public class BaseController {
    
    User loggedInUser = null;
    UserService userService;
    AuthenticationService authenticationService;

    public BaseController(UserService userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    private ErrorDTO setLoggedInUser(HttpServletRequest request) {

        try {
            String authHeader = request.getHeader("Authorization");

            if (authHeader == null) {
                throw new NotAuthorizedException("Authorization header is required.");
            }

            String[] headerParts = authHeader.split(" ");

            if (headerParts.length != 2 || !headerParts[0].equalsIgnoreCase("bearer")) {
                throw new NotAuthorizedException("Invalid authorization header format.");
            }

            String token = headerParts[1];
            DecodedJWT decoded = authenticationService.getDecodedToken(token);

            this.loggedInUser = this.userService.findUserById(Integer.parseInt(decoded.getSubject()));

            if (this.loggedInUser == null) {
                throw new NotAuthorizedException("User in your token does not exist.");
            }
        } 
        catch (ExpiredException ex) {
            return new ErrorDTO(401, ErrorType.UNAUTHORIZED, "Token expired", ex.getMessage());
        } 
        catch (SignatureInvalidException ex) {
            return new ErrorDTO(401, ErrorType.UNAUTHORIZED, "Invalid signature", ex.getMessage());
        } 
        catch (NotAuthorizedException ex) {
            return new ErrorDTO(401, ErrorType.UNAUTHORIZED, "Not authorized", ex.getMessage());
        } 
        
        return null;
    }

    public ErrorDTO atmLoggedInAuthorization(HttpServletRequest request){
        return this.setLoggedInUser(request);
    }
}
