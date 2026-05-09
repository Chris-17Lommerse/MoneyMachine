package MoneyMachine.controllers;

import MoneyMachine.models.enums.ErrorType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import MoneyMachine.models.User;
import MoneyMachine.models.dtos.ErrorDTO;
import MoneyMachine.models.dtos.LoginDTO;
import MoneyMachine.models.dtos.UserDTO;
import MoneyMachine.models.enums.LoginType;
import MoneyMachine.models.exceptions.InvalidCredentialsException;
import MoneyMachine.models.requestBodies.LoginRequestBody;
import MoneyMachine.services.Interfaces.AuthenticationService;
import MoneyMachine.services.Interfaces.UserService;
import jakarta.servlet.http.HttpServletRequest;

@RestController
@RequestMapping("/atm")
public class ATMAuthenticationController extends BaseController {
    
    private AuthenticationService authenticationService;

    public ATMAuthenticationController(AuthenticationService authenticationService, UserService userService){
        super(userService, authenticationService);
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestBody loginRequestBody) {

        try{
            User user = authenticationService.getUserByEmailAndPassword(loginRequestBody.getEmail(), loginRequestBody.getPassword());

            if (user == null){
                throw new InvalidCredentialsException("Password or username is not correct.");
            }

            LoginDTO loginDto = new LoginDTO(authenticationService.generateTokenFromUser(user), LoginType.atm);

            return ResponseEntity.status(201).body(loginDto);
        }
        catch (InvalidCredentialsException ex){

            ErrorDTO errorDTO = new ErrorDTO(401, ErrorType.UNAUTHORIZED, "Invalid credentials", ex.getMessage());

            return ResponseEntity.status(401).body(errorDTO);
        }
    }

    @GetMapping("/user-test")
    public ResponseEntity<UserDTO> userTest(HttpServletRequest request) {

        super.atmLoggedInAuthorization(request);

        UserDTO userDTO = new UserDTO(
            super.loggedInUser.getId(), 
            super.loggedInUser.getFirstName(),
            super.loggedInUser.getLastName(),
            super.loggedInUser.getEmail(),
            super.loggedInUser.getBsn(),
            super.loggedInUser.getPhoneNumber(),
            super.loggedInUser.getRole(),
            super.loggedInUser.getIsActive(),
            super.loggedInUser.getIsApproved()
        );

        return ResponseEntity.status(200).body(userDTO);
    }
}
