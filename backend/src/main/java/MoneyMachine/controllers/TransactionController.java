package MoneyMachine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MoneyMachine.mappers.BankAccountMapper;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.User;
import MoneyMachine.models.WithdrawTransaction;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.models.enums.Role;
import MoneyMachine.services.interfaces.AuthenticationService;
import MoneyMachine.services.interfaces.BankAccountService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {

    private BankAccountService bankAccountService;
    private AuthenticationService authenticationService;

    public TransactionController(BankAccountService bankAccountService, AuthenticationService authenticationService) {
        this.bankAccountService = bankAccountService;
        this.authenticationService = authenticationService;
    }

    @PostMapping("deposit")
    public ResponseEntity<DepositTransaction> deposit(@PathVariable String iban) {
        return null;
    }

    @PostMapping("withdraw")
    public ResponseEntity<WithdrawTransaction> withdraw(@PathVariable String iban) {
        return null;
    }
}