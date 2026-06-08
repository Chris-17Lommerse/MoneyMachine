package MoneyMachine.controllers;

import MoneyMachine.services.interfaces.TransactionService;
import org.springframework.http.ResponseEntity;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import MoneyMachine.exception.NotFoundException;
import MoneyMachine.models.dtos.responses.TransactionOverviewResponse;
import MoneyMachine.models.dtos.responses.UserOverviewResponse;
import MoneyMachine.services.interfaces.*;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("users")
public class UserController {

    private final TransactionService transactionService;
    private final UserService userService;
    private final BankAccountService bankAccountService;

    public UserController(UserService userService, TransactionService transactionService, AuthenticationService authenticationService, BankAccountService bankAccountService) {
        this.userService = userService;
        this.transactionService = transactionService;
        this.bankAccountService = bankAccountService;
    }

    @GetMapping("{id}/bank-accounts")
    @PreAuthorize("@authorizationService.isAllowedToInteractWithUserId(#id)")
    public ResponseEntity<BankAccountOverviewResponse> getBankAccountsByUserId(@PathVariable Long id, Pageable pageable) throws Exception {

        BankAccountOverviewResponse bankAccountOverviewResponse = bankAccountService.getAllBankAccountsByUserId(id, pageable);
        
        return ResponseEntity.status(200).body(bankAccountOverviewResponse);
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
    
    @GetMapping("/{id}/transactions")
    @PreAuthorize("@authorizationService.isLoggedIntoLoginType('WEBSITE')")
    public ResponseEntity<?> getTransactionsByUserId(@PathVariable Long id, Pageable pageable) throws Exception {
        
        TransactionOverviewResponse transactions = transactionService.getTransactionsByUserId(id, pageable);

        return ResponseEntity.status(200).body(transactions);
    }
}
