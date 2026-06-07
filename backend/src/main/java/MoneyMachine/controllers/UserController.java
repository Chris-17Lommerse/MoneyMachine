package MoneyMachine.controllers;

import java.util.List;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import MoneyMachine.models.dtos.requests.LoginRequest;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.models.dtos.responses.ErrorResponse;
import MoneyMachine.exception.NotFoundException;
import MoneyMachine.models.dtos.responses.LoginResponse;
import MoneyMachine.models.dtos.responses.TransactionResponse;
import MoneyMachine.models.dtos.responses.TransactionoverviewResponse;
import MoneyMachine.exception.NotFoundException;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import MoneyMachine.models.dtos.responses.UserOverviewResponse;
import MoneyMachine.models.dtos.responses.UserResponse;
import MoneyMachine.services.interfaces.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.ArrayList;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final BankAccountService bankAccountService;
    private final TransactionService transactionService;

    public UserController(UserService userService, AuthenticationService authenticationService, BankAccountService bankAccountService, TransactionService transactionService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
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
        
        List<UserResponse> users = userService.getAllUsersWithoutBankAccounts();

        if(users == null) {
            throw new NotFoundException("There are no users found in the database");
        }

        UserOverviewResponse userOverviewResponse = new UserOverviewResponse();
        userOverviewResponse.setUsers(users);

        return ResponseEntity.ok(userOverviewResponse);
    }
    public ResponseEntity<?> getAllUsersWithoutAnAccountTest() {
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
    @GetMapping("/{id}/transactions")
    @PreAuthorize("@authorizationService.isLoggedIntoLoginType('WEBSITE')")
    public ResponseEntity<?> getTransactionsByUserId(@PathVariable Long id, Pageable pageable) throws Exception {
        BankAccountOverviewResponse bankAccountOverviewResponse = bankAccountService.getAllBankAccountsByUserId(id, pageable);
        List<BankAccountResponse> bankAccounts = bankAccountOverviewResponse.getItems();
        TransactionoverviewResponse transactions = new TransactionoverviewResponse(new ArrayList<>(), pageable.getPageNumber(), pageable.getPageSize());
        for(BankAccountResponse bankAccount:bankAccounts)
        {
            String iban = bankAccount.getIban();
            TransactionoverviewResponse overview=transactionService.getTransactionsByIban(iban,pageable);
            for (TransactionResponse transactionResponse : overview.getTransactions()) {

                boolean isNewTransaction = true;

                for (TransactionResponse existing : transactions.getTransactions()) {

                    if (Objects.equals(existing.getTransactionId(),transactionResponse.getTransactionId())) {

                        isNewTransaction = false;
                        break;
                    }
                }

                if (isNewTransaction) {
                    transactions.getTransactions().add(transactionResponse);
                }
            }
        }
        return ResponseEntity.status(200).body(transactions);
    }
}
