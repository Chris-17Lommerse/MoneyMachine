package MoneyMachine.mappers;

import MoneyMachine.models.dtos.responses.TransactionResponse;
import MoneyMachine.models.dtos.requests.TransferRequest;
import org.springframework.stereotype.Component;
import MoneyMachine.models.*;
import MoneyMachine.models.WithdrawTransaction;
import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;
import MoneyMachine.models.dtos.responses.DepositTransactionResponse;

@Component
public class TransactionMapper {

    public TransactionResponse toResponse(Transaction t) {
        TransactionResponse response = new TransactionResponse();
        response.setTransactionId(t.getTransactionId());
        response.setAmount(t.getAmount());
        response.setMessage(t.getMessage());
        response.setTime(t.getDateTime());
        response.setInitiatedBy(String.valueOf(t.getInitiatingUser().getId()));
        return response;
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
        withdrawTransactionResponse.setAmount(withdrawTransaction.getAmount().doubleValue());
        withdrawTransactionResponse.setDateTime(withdrawTransaction.getDateTime());
        withdrawTransactionResponse.setIsActive(withdrawTransaction.getIsActive());

        return withdrawTransactionResponse;
    }

    public DepositTransactionResponse toDepositTransactionResponse(DepositTransaction depositTransaction) {

        DepositTransactionResponse depositTransactionResponse = new DepositTransactionResponse();
        depositTransactionResponse.setTransactionId(depositTransaction.getTransactionId());
        depositTransactionResponse.setInitiatingUserId(depositTransaction.getInitiatingUser().getId());
        depositTransactionResponse.setToAccountIban(depositTransaction.getToBankAccount().getIban());
        depositTransactionResponse.setMessage(depositTransaction.getMessage());
        depositTransactionResponse.setAmount(depositTransaction.getAmount().doubleValue());
        depositTransactionResponse.setDateTime(depositTransaction.getDateTime());
        depositTransactionResponse.setIsActive(depositTransaction.getIsActive());

        return depositTransactionResponse;
    }
}
