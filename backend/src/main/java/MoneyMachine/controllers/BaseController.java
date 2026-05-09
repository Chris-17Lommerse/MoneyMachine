package MoneyMachine.controllers;

import java.io.IOException;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import com.auth0.jwt.interfaces.DecodedJWT;

import MoneyMachine.models.User;
import MoneyMachine.models.exceptions.ExpiredException;
import MoneyMachine.models.exceptions.NotAuthorizedException;
import MoneyMachine.models.exceptions.SignatureInvalidException;
import MoneyMachine.services.Interfaces.AuthenticationService;
import MoneyMachine.services.Interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
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

    private void setLoggedInUser(HttpServletRequest request) {

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
            System.out.println(token);
            DecodedJWT decoded = authenticationService.getDecodedToken(token);

            this.loggedInUser = this.userService.findUserById(Integer.parseInt(decoded.getSubject()));

            if (this.loggedInUser == null) {
                throw new NotAuthorizedException("User in your token does not exist.");
            }
        } 
        catch (ExpiredException ex) {
            System.out.println(ex.getMessage());
            //sendErrorJson(response, 401, "Your token has expired.");
        } 
        catch (SignatureInvalidException ex) {
            System.out.println(ex.getMessage());
            //sendErrorJson(response, 401, "Token signature is not valid.");
        } 
        catch (NotAuthorizedException ex) {
            System.out.println(ex.getMessage());
            //sendErrorJson(response, 401, ex.getMessage());
        } 
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            //sendErrorJson(response, 400, ex.getMessage());
        }
    }

    public void atmLoggedInAuthorization(HttpServletRequest request){
        this.setLoggedInUser(request);
    }
}
