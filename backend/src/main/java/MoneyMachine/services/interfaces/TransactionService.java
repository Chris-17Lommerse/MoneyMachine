package MoneyMachine.services.interfaces;

import java.math.BigDecimal;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.requests.TransferRequest;
import MoneyMachine.models.dtos.responses.DepositTransactionResponse;
import MoneyMachine.models.dtos.responses.TransactionoverviewResponse;
import MoneyMachine.models.dtos.responses.TransactionResponse;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;

@Service
public interface TransactionService {
    DepositTransactionResponse depositAmountIntoBankAccount(String toIban, BigDecimal amount);
    WithdrawTransactionResponse withdrawAmountIntoBankAccount(String fromIban, BigDecimal amount);
    public TransactionoverviewResponse getAllTransactions(Pageable pageable);
    public TransactionoverviewResponse getTransactionsByIban(String iban,Pageable pageable);
    public TransactionResponse getTransactionByid(long id);
    public TransactionResponse createTransferAsUser(TransferRequest transaction,User user);
    public TransactionResponse createTransferAsEmployee(TransferRequest transaction,User user);
    
}
