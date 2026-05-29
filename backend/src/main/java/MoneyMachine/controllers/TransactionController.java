package MoneyMachine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MoneyMachine.models.User;
import MoneyMachine.models.dtos.requests.DepositRequest;
import MoneyMachine.models.dtos.requests.TransferRequest;
import MoneyMachine.models.dtos.requests.WithdrawRequest;
import MoneyMachine.models.dtos.responses.DepositTransactionResponse;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;
import MoneyMachine.services.AuthenticationServiceImpl;
import MoneyMachine.services.interfaces.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    
    private TransactionService transactionService;
    private AuthenticationServiceImpl authenticationService;

    public TransactionController(TransactionService transactionService, AuthenticationServiceImpl authenticationService) {
        this.transactionService = transactionService;
        this.authenticationService = authenticationService;
    }

    @GetMapping("")
     @PreAuthorize("hasRole('EMPLOYEE') && @authorizationService.isLoggedIntoLoginType('WEBSITE')")
    public ResponseEntity<?> getTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());  
    }

    @PostMapping("transfer")
     @PreAuthorize("@authorizationService.isLoggedIntoLoginType('WEBSITE')")
    public ResponseEntity<?> createTransfer(@RequestBody TransferRequest transaction) {
        User loggedInUser = authenticationService.getLoggedInUser();
        if(loggedInUser.getRole()==MoneyMachine.models.enums.Role.EMPLOYEE)
        {
            return ResponseEntity.ok(transactionService.createTransferAsEmployee(transaction,loggedInUser));
        }
       else if (loggedInUser.getRole()==MoneyMachine.models.enums.Role.USER)
        {
            return ResponseEntity.ok(transactionService.createTransferAsUser(transaction,loggedInUser));
        }
         
            
      

       
       return ResponseEntity.status(500).body("unknown role");
    }

    @PostMapping("deposit")
    @PreAuthorize("@authorizationService.isLoggedIntoLoginType('ATM')")
    public ResponseEntity<DepositTransactionResponse> deposit(@RequestBody DepositRequest depositRequest) {
        
        DepositTransactionResponse depositTransactionResponse = transactionService.depositAmountIntoBankAccount(depositRequest.getToBankAcountIban(), depositRequest.getAmount());

        return ResponseEntity.status(201).body(depositTransactionResponse);
    }

    @PostMapping("withdraw")
    @PreAuthorize("@authorizationService.isLoggedIntoLoginType('ATM')")
    public ResponseEntity<WithdrawTransactionResponse> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        
        WithdrawTransactionResponse withdrawTransactionResponse = transactionService.withdrawAmountIntoBankAccount(withdrawRequest.getFromBankAcountIban(), withdrawRequest.getAmount());

        return ResponseEntity.status(201).body(withdrawTransactionResponse);
    }
}
