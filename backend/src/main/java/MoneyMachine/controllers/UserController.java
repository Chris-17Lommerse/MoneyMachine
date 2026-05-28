package MoneyMachine.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import MoneyMachine.models.dtos.requests.LoginRequest;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import MoneyMachine.models.dtos.responses.ErrorResponse;
import MoneyMachine.models.dtos.responses.LoginResponse;
import MoneyMachine.models.dtos.responses.UserOverviewResponse;
import MoneyMachine.models.dtos.responses.UserResponse;
import org.springframework.data.domain.Pageable;
import MoneyMachine.services.interfaces.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final AuthenticationService authenticationService;
    private final BankAccountService bankAccountService;

    public UserController(UserService userService, AuthenticationService authenticationService, BankAccountService bankAccountService) {
        this.userService = userService;
        this.authenticationService = authenticationService;
        this.bankAccountService = bankAccountService;
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

    @GetMapping("{id}/bank-accounts")
    @PreAuthorize("@authorizationService.isAllowedToInteractWithUserId(#id)")
    public ResponseEntity<BankAccountOverviewResponse> getBankAccountsByUserId(@PathVariable Long id, Pageable pageable) throws Exception {

        BankAccountOverviewResponse bankAccountOverviewResponse = bankAccountService.getAllBankAccountsByUserId(id, pageable);
        
        return ResponseEntity.status(200).body(bankAccountOverviewResponse);
    }

    @GetMapping("employee-test")
    @PreAuthorize("hasRole('EMPLOYEE') && @authorizationService.isLoggedIntoLoginType('WEBSITE')")
    public ResponseEntity<String> employeeTest() {
        return ResponseEntity.status(200).body("You have the employee super powers that can power every power in the power universe and are website logged in.");
    }

    @GetMapping("{id}")
    @PreAuthorize("@authorizationService.isAllowedToInteractWithUserId(#id)")
    public ResponseEntity<String> getUserByIdTest(@PathVariable Long id) {
        return ResponseEntity.status(200).body("This is a test involving authorization based on conditionals put in @PreAuthorize and AuthorizationService.java.");
    }

    @GetMapping()
    @PreAuthorize("hasRole('EMPLOYEE') && @authorizationService.isLoggedIntoLoginType('WEBSITE')")
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
            ErrorResponse errorResponse = new ErrorResponse(500,
                    MoneyMachine.models.enums.ErrorType.INTERNAL_SERVER_ERROR,
                    "Internal Server Error - An unexpected error occurred", exInternalServerError.getMessage());
            return ResponseEntity.status(500).body(errorResponse);
        }
    }
}
