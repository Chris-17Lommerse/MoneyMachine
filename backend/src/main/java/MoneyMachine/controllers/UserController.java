package MoneyMachine.controllers;

import org.springframework.http.ResponseEntity;
import MoneyMachine.exception.NotFoundException;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import MoneyMachine.models.dtos.responses.UserOverviewResponse;
import MoneyMachine.models.dtos.responses.UserResponse;
import MoneyMachine.services.interfaces.*;

import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    private final UserService userService;
    private final BankAccountService bankAccountService;

    public UserController(UserService userService, BankAccountService bankAccountService) {
        this.userService = userService;
        this.bankAccountService = bankAccountService;
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
    public ResponseEntity<UserOverviewResponse> getAllUsersWithoutAnAccount(Pageable pageable) {
            UserOverviewResponse users = userService.getAllUsersWithoutBankAccounts(pageable);
            if(users == null)
            {
                throw new NotFoundException("There are no users found in the database");
            }
            return ResponseEntity.ok(users);
    }
}
