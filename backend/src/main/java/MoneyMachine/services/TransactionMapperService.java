package MoneyMachine.services;

import java.util.List;

import org.springframework.stereotype.Service;

import MoneyMachine.mappers.TransactionMapper;
import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.Transaction;
import MoneyMachine.models.TransferTransaction;
import MoneyMachine.models.WithdrawTransaction;
import MoneyMachine.models.dtos.requests.DepositRequest;
import MoneyMachine.models.dtos.requests.TransferRequest;
import MoneyMachine.models.dtos.requests.WithdrawRequest;
import MoneyMachine.models.dtos.responses.TransactionResponse;

@Service
public class TransactionMapperService {
    
    TransactionMapper mapper;
    TransactionMapperService(TransactionMapper mapper)
    {
        this.mapper = mapper;
    }
    public List<TransactionResponse> getAllTransactions( List<Transaction> transactions) {
        return transactions
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TransactionResponse toResponse(Transaction t) {
        TransactionResponse response = mapper.toResponse(t);
       switch (t) 
        {

            case TransferTransaction transfer -> 
            {
                response.setFromAccount(transfer.getFromBankAccount().getIban());
                response.setToAccount(transfer.getToBankAccount().getIban());
                response.setType("TRANSFER");
            }

            case WithdrawTransaction withdraw -> {
                response.setFromAccount(withdraw.getFromBankAccount().getIban());
                response.setType("WITHDRAW");
            }

            case DepositTransaction deposit -> {
                response.setToAccount(deposit.getToBankAccount().getIban());
                response.setType("DEPOSIT");
            }

            default -> throw new IllegalArgumentException(
                "Unknown type: " + t.getClass()
            );
        }

        return response;
    }
    public TransferTransaction toTransferEntity( TransferRequest transfer) {
       
      TransferTransaction t= mapper.toTransferEntity(transfer);
        t.setDateTime(java.time.LocalDateTime.now());
            return t;

    }
    public WithdrawTransaction toWithdrawEntity( WithdrawRequest withdraw) {
       
        WithdrawTransaction t= mapper.toWithdrawEntity(withdraw);
        t.setDateTime(java.time.LocalDateTime.now());
        return t;
    }
    public DepositTransaction toDepositEntity( DepositRequest deposit) {
       
        DepositTransaction t= mapper.toDepositEntity(deposit);
        t.setDateTime(java.time.LocalDateTime.now());
        return t;
    }
    

}
