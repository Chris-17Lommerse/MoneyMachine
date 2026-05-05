package MoneyMachine.models;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WithdrawTransaction extends Transaction {

    public WithdrawTransaction(long initiatingUserId, BigDecimal amount, String message, boolean isActive, long fromAccountIban) {
        super(initiatingUserId, amount, message, isActive); 
        this.fromAccountIban = fromAccountIban;
    }

    private long fromAccountIban;
}
