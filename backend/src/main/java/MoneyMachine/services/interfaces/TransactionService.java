package MoneyMachine.services.interfaces;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import MoneyMachine.models.dtos.requests.TransferRequest;
import MoneyMachine.models.dtos.responses.DepositTransactionResponse;
import MoneyMachine.models.dtos.responses.TransactionResponse;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;

@Service
public interface TransactionService {
    DepositTransactionResponse depositAmountIntoBankAccount(String toIban, BigDecimal amount);
    WithdrawTransactionResponse withdrawAmountIntoBankAccount(String fromIban, BigDecimal amount);
    public List<TransactionResponse> getAllTransactions();
    public List<TransactionResponse> getAllTransactionsByAccountId(String iban);
    public TransactionResponse getTransactionByid(long id);
    public TransactionResponse createTransfer(TransferRequest transaction);
    
}
