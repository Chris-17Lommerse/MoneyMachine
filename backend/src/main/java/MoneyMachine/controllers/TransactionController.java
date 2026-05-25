package MoneyMachine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import MoneyMachine.models.dtos.requests.TransferRequest;
import MoneyMachine.services.TransactionService;

@RestController
public class TransactionController {
    private TransactionService transactionService;
    
    public TransactionController(TransactionService transactionService) {
        this.transactionService = transactionService;
    }

    @GetMapping("/transactions")
    public ResponseEntity<?> getTransactions() {
        return ResponseEntity.ok(transactionService.getAllTransactions());  
    }

    @GetMapping("/transactions/account/{accountId}")
    public ResponseEntity<?> getTransactionsByAccountiban(@PathVariable String Iban)
    {
        return ResponseEntity.ok(transactionService.getAllTransactionsByAccountId(Iban));
    }

    @PostMapping("/transactions/transfer")
    public ResponseEntity<?> createTransfer(@RequestBody TransferRequest transaction) {
       
       return ResponseEntity.ok(transactionService.createTransfer(transaction));
    }

    @GetMapping("/transactions/{id}")
    public ResponseEntity<?> getTransactionById(@PathVariable Long id) { 
        return ResponseEntity.ok(transactionService.getTransactionByid(id));
    }
}
