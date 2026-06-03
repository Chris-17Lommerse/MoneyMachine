package MoneyMachine.services.interfaces;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import MoneyMachine.models.User;
import MoneyMachine.models.dtos.requests.TransferRequest;
import MoneyMachine.models.dtos.responses.DepositTransactionResponse;
import MoneyMachine.models.dtos.responses.TransactionResponse;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;

@Service
public interface TransactionService {
    DepositTransactionResponse depositAmountIntoBankAccount(String toIban, BigDecimal amount, String message);
    WithdrawTransactionResponse withdrawAmountIntoBankAccount(String fromIban, BigDecimal amount, String message);
    List<TransactionResponse> getAllTransactions();
    List<TransactionResponse> getAllTransactionsByAccountId(String iban);
    TransactionResponse getTransactionByid(long id);
    TransactionResponse createTransferAsUser(TransferRequest transaction, User user);
    TransactionResponse createTransferAsEmployee(TransferRequest transaction, User user);
}
