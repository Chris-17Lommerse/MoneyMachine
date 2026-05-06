package MoneyMachine.models;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class WithdrawTransaction extends Transaction {

    private BankAccount fromBankAccount;

    public WithdrawTransaction(long initiatingUserId, BigDecimal amount, String message, boolean isActive, BankAccount fromBankAccount) {
        super(initiatingUserId, amount, message, isActive); 
        this.fromBankAccount = fromBankAccount;
    }
}
