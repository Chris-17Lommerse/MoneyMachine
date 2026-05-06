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
public class TransferTransaction extends Transaction {

    public TransferTransaction(long initiatingUserId, BigDecimal amount, String message, boolean isActive, String fromAccountIban, String toAccountIban) {
        super(initiatingUserId, amount, message, isActive); 
        this.fromAccountIban = fromAccountIban;
        this.toAccountIban = toAccountIban;
    }

    private String fromAccountIban;
    private String toAccountIban;
}
