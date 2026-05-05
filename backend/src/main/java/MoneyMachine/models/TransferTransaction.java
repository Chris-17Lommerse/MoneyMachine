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
public class TransferTransaction extends Transaction {

    public TransferTransaction(long initiatingUserId, BigDecimal amount, String message, boolean isActive, long fromAccountIban, long toAccountIban) {
        super(initiatingUserId, amount, message, isActive); 
        this.fromAccountIban = fromAccountIban;
        this.toAccountIban = toAccountIban;
    }

    private long fromAccountIban;
    private long toAccountIban;
}
