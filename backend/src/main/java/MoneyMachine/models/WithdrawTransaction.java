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

    public WithdrawTransaction(long initiatingUserId, BigDecimal amount, String message, boolean isActive, String fromAccountIban) {
        super(initiatingUserId, amount, message, isActive); 
        this.fromAccountIban = fromAccountIban;
    }

    private String fromAccountIban;
}
