package MoneyMachine.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import MoneyMachine.models.dtos.requests.BankAccountCreationRequest;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.services.interfaces.BankAccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;

@RestController
@RequestMapping("/bank-accounts")
public class BankAccountController {
    private BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
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