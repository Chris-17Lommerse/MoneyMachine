package MoneyMachine.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import MoneyMachine.exception.InvalidCredentialsException;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.LoginResponse;
import MoneyMachine.models.dtos.UserResponse;
import MoneyMachine.models.enums.LoginType;
import MoneyMachine.models.requestBodies.LoginRequest;
import MoneyMachine.services.UserServiceImpl;
import MoneyMachine.services.Interfaces.AuthenticationService;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/users")
public class UsersController {

    private final UserServiceImpl userService;
    private final AuthenticationService authenticationService;

    public UsersController(UserServiceImpl userService, AuthenticationService authenticationService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequestBody) throws Exception {

        User user = authenticationService.getUserByEmailAndPassword(loginRequestBody.getEmail(), loginRequestBody.getPassword());

        if (user == null){
            throw new InvalidCredentialsException("Password or username is not correct.");
        }

        LoginResponse loginDto = new LoginResponse(authenticationService.generateAuthTokenFromUser(user));

        return ResponseEntity.status(201).body(loginDto);
    }

    @GetMapping("/me")
    public ResponseEntity<?> getLoggedInUser(HttpServletRequest request, HttpServletResponse response, @RequestParam LoginType loginType) throws Exception {

        User user = this.authenticationService.getLoggedInUserByLoginType(request, response, loginType);

        UserResponse userDTO = new UserResponse(
            user.getId(), 
            user.getFirstName(),
            user.getLastName(),
            user.getEmail(),
            user.getBsn(),
            user.getPhoneNumber(),
            user.getRole(),
            user.getIsActive(),
            user.getIsApproved()
        );

        return ResponseEntity.status(200).body(userDTO);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getAllUsersWithoutAnAccount() 
    {
        try
        {
            List<User> users = userService.getAllUsersWithoutAnAccount();
            return ResponseEntity.status(200).body(users);
        } catch (InternalServerError ex)
        {
            return ResponseEntity.status(500).body(ex);
        }
        catch (Unauthorized ex)
        {
            return ResponseEntity.status(401).body(ex);
        }
    }
}
