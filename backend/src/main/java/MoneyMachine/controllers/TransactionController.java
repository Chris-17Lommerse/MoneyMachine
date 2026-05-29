package MoneyMachine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MoneyMachine.models.dtos.requests.DepositRequest;
import MoneyMachine.models.dtos.requests.TransferRequest;
import MoneyMachine.models.dtos.requests.WithdrawRequest;
import MoneyMachine.models.dtos.responses.DepositTransactionResponse;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;
import MoneyMachine.services.interfaces.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    
    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("")
    public ResponseEntity<?> getTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());  
    }

    @PostMapping("transfer")
    public ResponseEntity<?> createTransfer(@RequestBody TransferRequest transaction) {
       
       return ResponseEntity.ok(transactionService.createTransfer(transaction));
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
