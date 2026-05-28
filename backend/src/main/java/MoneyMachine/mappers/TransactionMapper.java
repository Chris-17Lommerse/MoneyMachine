package MoneyMachine.mappers;

import MoneyMachine.models.dtos.responses.TransactionResponse;
import MoneyMachine.models.dtos.requests.DepositRequest;
import MoneyMachine.models.dtos.requests.TransferRequest;
import MoneyMachine.models.dtos.requests.WithdrawRequest;
import org.springframework.stereotype.Component;

import MoneyMachine.models.*;

@Component
public class TransactionMapper {

   
    public TransactionResponse toResponse(Transaction t)
    {
        TransactionResponse response = new TransactionResponse();
        response.setTransactionId(t.getTransactionId());
        response.setAmount(t.getAmount());
        response.setMessage(t.getMessage());
        return response;


    }


    public TransferTransaction toTransferEntity(TransferRequest r)
    {
        TransferTransaction transfer = new TransferTransaction();
        transfer.setAmount(r.getAmount());
        transfer.setMessage(r.getMessage());
        return transfer;
    }

    public WithdrawTransaction toWithdrawEntity(WithdrawRequest r)
    {
        WithdrawTransaction withdraw = new WithdrawTransaction();
        withdraw.setAmount(r.getAmount());
        withdraw.setMessage(r.getMessage());
        return withdraw;
    }

    public DepositTransaction toDepositEntity(DepositRequest r)
    {
        DepositTransaction deposit = new DepositTransaction();
        deposit.setAmount(r.getAmount());
        deposit.setMessage(r.getMessage());
        return deposit;
    }
}
