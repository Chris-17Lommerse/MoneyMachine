package MoneyMachine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MoneyMachine.mappers.BankAccountMapper;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.services.interfaces.BankAccountService;

@RestController
@RequestMapping("/bank-accounts")
public class BankAccountController {

    private BankAccountService bankAccountService;
    private BankAccountMapper bankAccountMapper;

    public BankAccountController(BankAccountService bankAccountService, BankAccountMapper bankAccountMapper) {
        this.bankAccountService = bankAccountService;
        this.bankAccountMapper = bankAccountMapper;
    }

    @GetMapping("{iban}")
    // @PreAuthorize("@authorizationService.isAllowedToInteractWithUserId(#iban)")
    public ResponseEntity<BankAccountResponse> getBankAccountByIban(@PathVariable String iban) {
        
        BankAccount bankAccount = bankAccountService.getBankAccountByIban(iban);
        return ResponseEntity.status(200).body(bankAccountMapper.toResponse(bankAccount));
    }
}