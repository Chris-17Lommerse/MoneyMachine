package MoneyMachine.services.interfaces;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import MoneyMachine.models.dtos.responses.DepositTransactionResponse;
import MoneyMachine.models.dtos.responses.WithdrawTransactionResponse;

@Service
public interface TransactionService {
    DepositTransactionResponse depositAmountIntoBankAccount(String toIban, BigDecimal amount);
    WithdrawTransactionResponse withdrawAmountIntoBankAccount(String fromIban, BigDecimal amount);
}
