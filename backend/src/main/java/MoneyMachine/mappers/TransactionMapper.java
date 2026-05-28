package MoneyMachine.mappers;

import org.springframework.stereotype.Component;

import MoneyMachine.models.WithdrawTransaction;
import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;
import MoneyMachine.models.dtos.responses.DepositTransactionResponse;

@Component
public class TransactionMapper {

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