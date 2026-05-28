package MoneyMachine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MoneyMachine.mappers.BankAccountMapper;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.models.enums.Role;
import MoneyMachine.services.interfaces.AuthenticationService;
import MoneyMachine.services.interfaces.BankAccountService;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import MoneyMachine.models.dtos.requests.BankAccountCreationRequest;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/bank-accounts")
public class BankAccountController {

    private BankAccountService bankAccountService;
    private BankAccountMapper bankAccountMapper;
    private AuthenticationService authenticationService;

    public BankAccountController(BankAccountService bankAccountService, BankAccountMapper bankAccountMapper, AuthenticationService authenticationService) {
        this.bankAccountService = bankAccountService;
        this.bankAccountMapper = bankAccountMapper;
        this.authenticationService = authenticationService;
    }

    @GetMapping("{iban}")
    public ResponseEntity<BankAccountResponse> getBankAccountByIban(@PathVariable String iban) {
        
        BankAccount bankAccount;
        User loggedInUser = this.authenticationService.getLoggedInUser();

        if (loggedInUser.getRole() == Role.USER) {
            bankAccount = bankAccountService.getBankAccountByIbanAndUserId(iban, loggedInUser.getId());
        }
        else {
            bankAccount = bankAccountService.getBankAccountByIban(iban);
        }

        return ResponseEntity.status(200).body(bankAccountMapper.toResponse(bankAccount));
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
}