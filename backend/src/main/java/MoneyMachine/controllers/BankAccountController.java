package MoneyMachine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import MoneyMachine.models.BankAccount;
import MoneyMachine.models.dtos.requests.BankAccountCreationRequest;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.services.interfaces.BankAccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;


@Controller
@RequestMapping("bank-accounts")
public class BankAccountController extends BaseController
{
    private BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService)
    {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping()
    public ResponseEntity<BankAccountResponse> createBankAccount(@RequestBody BankAccountCreationRequest bankAccountCreationRequest) throws Exception{
        BankAccountResponse bankAccountResponse = new BankAccountResponse(bankAccountService.createBankAccount(0, null, null));
        return ResponseEntity.status(201).body(bankAccountResponse);
    }
    
}