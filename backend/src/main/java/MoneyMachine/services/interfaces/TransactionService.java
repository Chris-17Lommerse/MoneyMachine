package MoneyMachine.services.interfaces;

import java.math.BigDecimal;

import org.springframework.data.domain.Pageable;

import MoneyMachine.models.dtos.requests.FilterRequest;
import MoneyMachine.models.dtos.responses.DepositTransactionResponse;
import MoneyMachine.models.dtos.responses.TransactionOverviewResponse;
import MoneyMachine.models.dtos.responses.TransferTransactionResponse;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;

public interface TransactionService {
    TransferTransactionResponse transferAmountBetweenBankAccounts(String fromIban, String toIban, BigDecimal amount, String message);
    DepositTransactionResponse depositAmountIntoBankAccount(String toIban, BigDecimal amount);
    WithdrawTransactionResponse withdrawAmountIntoBankAccount(String fromIban, BigDecimal amount);
    TransactionOverviewResponse getAllTransactions(Pageable pageable, FilterRequest filter);
    TransactionOverviewResponse getTransactionsByIban(String iban,Pageable pageable, FilterRequest filter);
    TransactionOverviewResponse getTransactionsByUserId(Long id, Pageable pageable, FilterRequest filter);
}
