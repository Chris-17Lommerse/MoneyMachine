package MoneyMachine.controllers;

import org.springframework.http.ResponseEntity;
import MoneyMachine.models.dtos.requests.LoginRequest;
import MoneyMachine.models.dtos.responses.LoginResponse;
import MoneyMachine.models.dtos.responses.UserResponse;
import MoneyMachine.services.interfaces.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("auth")
public class AuthController {

    private final AuthenticationService authenticationService;

    public AuthController(AuthenticationService authenticationService) {
        this.authenticationService = authenticationService;
    }

    @PostMapping("login")
    public ResponseEntity<LoginResponse> login(@RequestBody LoginRequest loginRequest) throws Exception {

        LoginResponse loginResponse = authenticationService.login(loginRequest.getEmail(), loginRequest.getPassword(), loginRequest.getLoginType());

        return ResponseEntity.status(201).body(loginResponse);
    }

    @GetMapping("me")
    public ResponseEntity<?> getLoggedInUser(HttpServletRequest request, HttpServletResponse response) throws Exception {

        UserResponse userResponse = authenticationService.getLoggedInUserResponse();

        return ResponseEntity.status(200).body(userResponse);
    }

    @GetMapping("employee-test")
    @PreAuthorize("hasRole('EMPLOYEE') && @authorizationService.isLoggedIntoLoginType('WEBSITE')")
    public ResponseEntity<String> employeeTest() {
        return ResponseEntity.status(200).body("You have the employee super powers that can power every power in the power universe and are website logged in.");
    }
}
