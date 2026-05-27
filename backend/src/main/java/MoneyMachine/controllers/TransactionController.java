package MoneyMachine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MoneyMachine.mappers.TransactionMapper;
import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.WithdrawTransaction;
import MoneyMachine.models.dtos.requests.DepositRequest;
import MoneyMachine.models.dtos.requests.WithdrawRequest;
import MoneyMachine.models.dtos.responses.DepositTransactionResponse;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;
import MoneyMachine.services.interfaces.TransactionService;

@RestController
@RequestMapping("/transactions")
public class TransactionController {
    
    private TransactionService transactionService;
    private TransactionMapper transactionMapper;

    public TransactionController(TransactionService transactionService, TransactionMapper transactionMapper) {
        this.transactionService = transactionService;
        this.transactionMapper = transactionMapper;
    }

    @PostMapping("deposit")
    @PreAuthorize("@authorizationService.isLoggedIntoLoginType('ATM')")
    public ResponseEntity<DepositTransactionResponse> deposit(@RequestBody DepositRequest depositRequest) {
        
        DepositTransaction depositTransaction = transactionService.depositAmountIntoBankAccount(depositRequest.getToBankAcountIban(), depositRequest.getAmount());

        return ResponseEntity.status(201).body(transactionMapper.toDepositTransactionResponse(depositTransaction));
    }

    @PostMapping("withdraw")
    @PreAuthorize("@authorizationService.isLoggedIntoLoginType('ATM')")
    public ResponseEntity<WithdrawTransactionResponse> withdraw(@RequestBody WithdrawRequest withdrawRequest) {
        
        WithdrawTransaction withdrawTransaction = transactionService.withdrawAmountIntoBankAccount(withdrawRequest.getFromBankAcountIban(), withdrawRequest.getAmount());

        return ResponseEntity.status(201).body(transactionMapper.toWithdrawTransactionResponse(withdrawTransaction));
    }
}