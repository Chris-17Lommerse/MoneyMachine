package MoneyMachine.models;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Transaction {
    @Id
    @GeneratedValue
    private long transactionId;
    private long initiatingUserId;
    private BigDecimal amount;
    private String message;
    private Boolean isActive;

    public Transaction(long initiatingUserId, BigDecimal amount, String message, Boolean isActive) {
        this.initiatingUserId = initiatingUserId;
        this.amount = amount;
        this.message = message;
        this.isActive = isActive;
    }
}
