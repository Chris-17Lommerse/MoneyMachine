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
public class DepositTransaction extends Transaction {

    public DepositTransaction(long initiatingUserId, BigDecimal amount, String message, boolean isActive, long toAccountIban) {
        super(initiatingUserId, amount, message, isActive); 
        this.toAccountIban = toAccountIban;
    }

    private long toAccountIban;
}
