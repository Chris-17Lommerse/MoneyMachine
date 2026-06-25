package MoneyMachine.controllers;

import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import MoneyMachine.models.dtos.requests.DepositRequest;
import MoneyMachine.models.dtos.requests.FilterRequest;
import MoneyMachine.models.dtos.requests.TransferRequest;
import MoneyMachine.models.dtos.requests.WithdrawRequest;
import MoneyMachine.models.dtos.responses.DepositTransactionResponse;
import MoneyMachine.models.dtos.responses.TransferTransactionResponse;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;
import MoneyMachine.services.interfaces.TransactionService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    
    private TransactionService transactionService;

    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("")
    @PreAuthorize("hasRole('EMPLOYEE') && @authorizationService.isLoggedIntoLoginType('WEBSITE')")
    public ResponseEntity<?> getTransactions(@PageableDefault(page = 0, size = 20)Pageable pageable, @ModelAttribute FilterRequest filter) {
        return ResponseEntity.ok(transactionService.getAllTransactions(pageable,filter));  
    }
    @PostMapping("transfer")
    @PreAuthorize("@authorizationService.isLoggedIntoLoginType('WEBSITE')")
    public ResponseEntity<?> createTransfer(@RequestBody @Valid TransferRequest transferRequest) {
        
        TransferTransactionResponse transferTransactionResponse = transactionService.transferAmountBetweenBankAccounts(transferRequest.getFromBankAcountIban(), transferRequest.getToBankAcountIban(), transferRequest.getAmount(), transferRequest.getMessage());
         
        return ResponseEntity.status(201).body(transferTransactionResponse);
    }

    @PostMapping("deposit")
    @PreAuthorize("@authorizationService.isLoggedIntoLoginType('ATM')")
    public ResponseEntity<DepositTransactionResponse> deposit(@RequestBody @Valid DepositRequest depositRequest) {
        
        DepositTransactionResponse depositTransactionResponse = transactionService.depositAmountIntoBankAccount(depositRequest.getToBankAcountIban(), depositRequest.getAmount());

        return ResponseEntity.status(201).body(depositTransactionResponse);
    }

    @PostMapping("withdraw")
    @PreAuthorize("@authorizationService.isLoggedIntoLoginType('ATM')")
    public ResponseEntity<WithdrawTransactionResponse> withdraw(@RequestBody @Valid WithdrawRequest withdrawRequest) {
        
        WithdrawTransactionResponse withdrawTransactionResponse = transactionService.withdrawAmountIntoBankAccount(withdrawRequest.getFromBankAcountIban(), withdrawRequest.getAmount());

        return ResponseEntity.status(201).body(withdrawTransactionResponse);
    }

    /*
        ALTERNATIVE IMPLEMENTATION IF AN UNIFIED ENDPOINT WOULD BE IMPLEMENTED THAT HAS NOT BEEN CHOSEN
        
        DRAWBACKS OF USING THE IMPLEMENTATION BELOW:
            * MANUAL AUTHORIZATON CHECK LOGIC BASED ON TRANSACTION TYPE 
                (Deposit and withdraw can only happen when logged into atm while website login is required for transfer)
            * Nullables are present in the requestBody
            * Needing to first check the transaction type based on nullable Ibans
            * Seperation of concerns violation due to one method handling transactions, deposits and withdraws
            * Unknown ResponseEntity type
            * More code
    */

    // @PostMapping("transaction")
    // public ResponseEntity<?> createTransaction(@RequestBody UnifiedRequest req) {

    //     TransactionType type = inferType(req);

    //     if (type == DEPOSIT || type == WITHDRAW) {
    //         if (!authorizationService.isLoggedIntoLoginType("ATM")) {
    //             return ResponseEntity.status(403).body("ATM login required");
    //         }
    //     } 
    //     else if (!authorizationService.isLoggedIntoLoginType("WEBSITE")) {
    //             return ResponseEntity.status(403).body("Website login required");
    //     }

    //     return switch (type) {
    //         case DEPOSIT  -> transactionService.deposit(req.getToIban(), ...);
    //         case WITHDRAW -> transactionService.withdraw(req.getFromIban(), ...);
    //         case TRANSFER -> transactionService.createTransfer(...);
    //     };
    // }

    // private TransactionType inferType(UnifiedRequest req) {

    //     if (req.getFromIban() == null && req.getToIban() != null) {
    //         return DEPOSIT;  
    //     } 
    //     else if (req.getToIban() == null && req.getFromIban() != null) {
    //         return WITHDRAW;
    //     }
    //     else if (req.getFromIban() != null && req.getToIban() != null) {
    //         return TRANSFER;
    //     }

    //     throw new IllegalArgumentException("Cannot determine transaction type");
    // }
}
