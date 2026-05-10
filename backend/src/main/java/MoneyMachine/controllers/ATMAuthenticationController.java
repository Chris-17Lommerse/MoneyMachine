package MoneyMachine.controllers;

import MoneyMachine.models.enums.ErrorType;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import MoneyMachine.models.User;
import MoneyMachine.models.dtos.LoginDTO;
import MoneyMachine.models.dtos.UserDTO;
import MoneyMachine.models.enums.LoginType;
import MoneyMachine.models.exceptions.InvalidCredentialsException;
import MoneyMachine.models.requestBodies.LoginRequestBody;
import MoneyMachine.services.Interfaces.AuthenticationService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/atm")
public class ATMAuthenticationController extends BaseController {
    
    private AuthenticationService authenticationService;

    public ATMAuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login(@RequestBody LoginRequestBody loginRequestBody) throws Exception {

        User user = authenticationService.getUserByEmailAndPassword(loginRequestBody.getEmail(), loginRequestBody.getPassword());

        if (user == null){
            throw new InvalidCredentialsException("Password or username is not correct.");
        }

        LoginDTO loginDto = new LoginDTO(authenticationService.generateAuthTokenFromUser(user), LoginType.atm);

        return ResponseEntity.status(201).body(loginDto);
    }

    @GetMapping("/user-test")
    public ResponseEntity<?> userTest(HttpServletRequest request, HttpServletResponse response) throws Exception {

        User loggedInAtmUser = this.authenticationService.getLoggedInAtmUser(request, response);

        UserDTO userDTO = new UserDTO(
            loggedInAtmUser.getId(), 
            loggedInAtmUser.getFirstName(),
            loggedInAtmUser.getLastName(),
            loggedInAtmUser.getEmail(),
            loggedInAtmUser.getBsn(),
            loggedInAtmUser.getPhoneNumber(),
            loggedInAtmUser.getRole(),
            loggedInAtmUser.getIsActive(),
            loggedInAtmUser.getIsApproved()
        );

        return ResponseEntity.status(200).body(userDTO);
    }
}
