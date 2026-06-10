package MoneyMachine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MoneyMachine.models.User;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.models.enums.Role;
import MoneyMachine.services.interfaces.AuthenticationService;
import MoneyMachine.services.interfaces.BankAccountService;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.access.prepost.PreAuthorize;
import MoneyMachine.models.dtos.requests.BankAccountCreationRequest;
import MoneyMachine.models.dtos.requests.PatchRequest;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import MoneyMachine.services.interfaces.TransactionService;

import org.springframework.web.bind.annotation.PatchMapping;

@RestController
@RequestMapping("/bank-accounts")
public class BankAccountController {

    private BankAccountService bankAccountService;
    private AuthenticationService authenticationService;
    private TransactionService transactionService;

    public BankAccountController(BankAccountService bankAccountService, AuthenticationService authenticationService, TransactionService transactionService) {
        this.bankAccountService = bankAccountService;
        this.authenticationService = authenticationService;
        this.transactionService = transactionService;
    }

    @PostMapping()
    @PreAuthorize("hasRole('EMPLOYEE') && @authorizationService.isLoggedIntoLoginType('WEBSITE')")
    public ResponseEntity<BankAccountResponse> createBankAccount(
            @RequestBody BankAccountCreationRequest bankAccountCreationRequest) throws Exception {
        BankAccountResponse bankAccountResponse = bankAccountService
                .createBankAccountFromRequest(bankAccountCreationRequest);
        return ResponseEntity.status(201).body(bankAccountResponse);
    }

    @GetMapping()
    @PreAuthorize("hasRole('EMPLOYEE') && @authorizationService.isLoggedIntoLoginType('WEBSITE')")
    public ResponseEntity<BankAccountOverviewResponse> getAllBankAccounts(Pageable pageable) {
        BankAccountOverviewResponse bankAccounts = bankAccountService.getAllBankAccounts(pageable);
        return ResponseEntity.ok(bankAccounts);
    }

    @GetMapping("/{iban}/transactions")
    @PreAuthorize("@authorizationService.isLoggedIntoLoginType('WEBSITE')")
    public ResponseEntity<?> getTransactionsByIban(@PathVariable String iban,@PageableDefault(page = 0, size = 20) Pageable pageable) {
        return ResponseEntity.ok(transactionService.getTransactionsByIban(iban, pageable));
    }

    @GetMapping("/{iban}")
    public ResponseEntity<BankAccountResponse> getBankAccountByIban(@PathVariable String iban) {

        BankAccountResponse bankAccountResponse;
        User loggedInUser = authenticationService.getLoggedInUser();

        if (loggedInUser.getRole() == Role.USER) {
            bankAccountResponse = bankAccountService.getBankAccountByIbanAndUserId(iban, loggedInUser.getId());
        } 
        else {
            bankAccountResponse = bankAccountService.getBankAccountByIban(iban);
        }

        return ResponseEntity.ok(bankAccountResponse);
    }
    
    @PatchMapping("/{iban}")
    @PreAuthorize("hasRole('EMPLOYEE') && @authorizationService.isLoggedIntoLoginType('WEBSITE')")
    public ResponseEntity<BankAccountResponse> closeBankAccount(@RequestBody PatchRequest patchRequest,  @PathVariable String iban)
    {
        BankAccountResponse bankAccountResponse = bankAccountService.closeBankAccount(patchRequest, iban);
        return ResponseEntity.ok(bankAccountResponse);
    }
    
    
}