package MoneyMachine.services.interfaces;

import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.WithdrawTransaction;

@Service
public interface TransactionService {
    DepositTransaction depositAmountIntoBankAccount(String toIban, BigDecimal amount);
    WithdrawTransaction withdrawAmountIntoBankAccount(String fromIban, BigDecimal amount);
}
