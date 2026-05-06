package MoneyMachine.models;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "transactions")
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@Getter
@Setter
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
