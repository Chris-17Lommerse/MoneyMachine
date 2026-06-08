package MoneyMachine.mappers;

import MoneyMachine.models.dtos.responses.TransferTransactionResponse;
import MoneyMachine.models.dtos.requests.TransferRequest;
import org.springframework.stereotype.Component;
import MoneyMachine.models.*;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;
import MoneyMachine.models.dtos.responses.DepositTransactionResponse;

@Component
public class TransactionMapper {

    public TransferTransactionResponse toResponse(Transaction t) {

        TransferTransactionResponse response = new TransferTransactionResponse();
        response.setTransactionId(t.getTransactionId());
        response.setAmount(t.getAmount());
        response.setMessage(t.getMessage());
        response.setDateTime(t.getDateTime());
        response.setInitiatingUserId(t.getInitiatingUser().getId());
        return response;
    }

    public TransferTransactionResponse toTransferTransactionResponse(TransferTransaction transferTransaction) {

        TransferTransactionResponse transferTransactionResponse = new TransferTransactionResponse();
        transferTransactionResponse.setTransactionId(transferTransaction.getTransactionId());
        transferTransactionResponse.setInitiatingUserId(transferTransaction.getInitiatingUser().getId());
        transferTransactionResponse.setFromAccountIban(transferTransaction.getFromBankAccount().getIban());
        transferTransactionResponse.setFromAccountIban(transferTransaction.getToBankAccount().getIban());
        transferTransactionResponse.setMessage(transferTransaction.getMessage());
        transferTransactionResponse.setAmount(transferTransaction.getAmount());
        transferTransactionResponse.setDateTime(transferTransaction.getDateTime());

        return transferTransactionResponse;
    }

    public TransferTransaction toTransferEntity(TransferRequest r) {
        
        TransferTransaction transfer = new TransferTransaction();
        transfer.setAmount(r.getAmount());
        transfer.setMessage(r.getMessage());
        
        return transfer;
    }

    public WithdrawTransactionResponse toWithdrawTransactionResponse(WithdrawTransaction withdrawTransaction) {

        WithdrawTransactionResponse withdrawTransactionResponse = new WithdrawTransactionResponse();
        withdrawTransactionResponse.setTransactionId(withdrawTransaction.getTransactionId());
        withdrawTransactionResponse.setInitiatingUserId(withdrawTransaction.getInitiatingUser().getId());
        withdrawTransactionResponse.setFromAccountIban(withdrawTransaction.getFromBankAccount().getIban());
        withdrawTransactionResponse.setMessage(withdrawTransaction.getMessage());
        withdrawTransactionResponse.setAmount(withdrawTransaction.getAmount());
        withdrawTransactionResponse.setDateTime(withdrawTransaction.getDateTime());

        return withdrawTransactionResponse;
    }

    public DepositTransactionResponse toDepositTransactionResponse(DepositTransaction depositTransaction) {

        DepositTransactionResponse depositTransactionResponse = new DepositTransactionResponse();
        depositTransactionResponse.setTransactionId(depositTransaction.getTransactionId());
        depositTransactionResponse.setInitiatingUserId(depositTransaction.getInitiatingUser().getId());
        depositTransactionResponse.setToAccountIban(depositTransaction.getToBankAccount().getIban());
        depositTransactionResponse.setMessage(depositTransaction.getMessage());
        depositTransactionResponse.setAmount(depositTransaction.getAmount());
        depositTransactionResponse.setDateTime(depositTransaction.getDateTime());

        return depositTransactionResponse;
    }
}
