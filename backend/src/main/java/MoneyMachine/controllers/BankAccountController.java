package MoneyMachine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MoneyMachine.models.dtos.requests.BankAccountCreationRequest;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.services.interfaces.BankAccountService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@RestController
@RequestMapping("bank-accounts")
public class BankAccountController extends BaseController {
    private BankAccountService bankAccountService;

    public BankAccountController(BankAccountService bankAccountService) {
        this.bankAccountService = bankAccountService;
    }

    @PostMapping()
    public ResponseEntity<BankAccountResponse> createBankAccount(
            @RequestBody BankAccountCreationRequest bankAccountCreationRequest) throws Exception {
        BankAccountResponse bankAccountResponse = bankAccountService.createBankAccountFromRequest(bankAccountCreationRequest);
        return ResponseEntity.status(201).body(bankAccountResponse);
    }

    @GetMapping()
    public ResponseEntity<BankAccountOverviewResponse> getAllBankAccounts()
    {
        try
        {
            List<BankAccountResponse> bankAccounts = bankAccountService.getAllBankAccounts();
            BankAccountOverviewResponse bankAccountOverviewResponse = new BankAccountOverviewResponse();
            bankAccountOverviewResponse.setBankAccounts(bankAccounts);
            return ResponseEntity.ok(bankAccountOverviewResponse);
        }
        catch
        {

        }
    }
    
}