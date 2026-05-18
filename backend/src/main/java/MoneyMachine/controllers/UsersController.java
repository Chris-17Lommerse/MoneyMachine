package MoneyMachine.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import MoneyMachine.exception.InvalidCredentialsException;
import MoneyMachine.mappers.UserMapper;
import MoneyMachine.models.User;
import MoneyMachine.models.enums.LoginType;
import MoneyMachine.models.dtos.requests.LoginRequest;
import MoneyMachine.models.dtos.responses.ErrorResponse;
import MoneyMachine.models.dtos.responses.LoginResponse;
import MoneyMachine.models.dtos.responses.UserOverviewResponse;
import MoneyMachine.models.dtos.responses.UserResponse;
import MoneyMachine.services.interfaces.*;
import MoneyMachine.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("users")
public class UsersController {

    private final JwtUtil jwtUtil;
    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final UserMapper userMapper;

    public UsersController(UserService userService, AuthenticationService authenticationService, UserMapper userMapper, JwtUtil jwtUtil) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {

        User user = authenticationService.getUserByEmailAndPassword(loginRequest.getEmail(), loginRequest.getPassword());

        if (user == null){
            throw new InvalidCredentialsException("Password or username is not correct.");
        }

        LoginResponse loginResponse = new LoginResponse(jwtUtil.generateAuthTokenFromUser(user, loginRequest.getLoginType()));

        return ResponseEntity.status(201).body(loginResponse);
    }

    @GetMapping("me")
    public ResponseEntity<?> getLoggedInUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

        User user = (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        UserResponse userResponse = userMapper.toResponse(user);
        
        return ResponseEntity.status(200).body(userResponse);
    }

    @GetMapping("employee-test")
    @PreAuthorize("hasRole('EMPLOYEE')")
    public ResponseEntity<String> employeeTest() {
        return ResponseEntity.status(200).body("Yes yo have employee powers");
    }

    @GetMapping("/{id}")
    @PreAuthorize("@authorizationService.isAllowedToGetUserById(authentication, #id)")
    public ResponseEntity<String> getUserByIdTest() {
        return ResponseEntity.status(200).body("You are allowed to get this user");
    }

    @GetMapping()
    public ResponseEntity<?> getAllUsersWithoutAnAccount() {
        try {
            List<UserResponse> users = userService.getAllUsersWithoutBankAccounts();
            UserOverviewResponse userOverviewResponse = new UserOverviewResponse();
            userOverviewResponse.setUsers(users);
            return ResponseEntity.ok(userOverviewResponse);
        } catch (Unauthorized exUnauthorized) {
            ErrorResponse errorResponse = new ErrorResponse(401, MoneyMachine.models.enums.ErrorType.UNAUTHORIZED,
                    "Unauthorized - Authentication required", exUnauthorized.getMessage());
            return ResponseEntity.status(401).body(errorResponse);
        } catch (InternalServerError exInternalServerError) {
            ErrorResponse errorResponse = new ErrorResponse(500, MoneyMachine.models.enums.ErrorType.INTERNAL_SERVER_ERROR,
                    "Internal Server Error - An unexpected error occurred", exInternalServerError.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
