package MoneyMachine.models;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    private long transactionId;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private User initiatingUser;

    @Column(nullable = false)
    @NotNull
    private BigDecimal amount;

    @Column(nullable = false, length = 255)
    @NotNull
    private String message;

    @Column(nullable = false)
    @NotNull
    private Boolean isActive;

    public Transaction(User initiatingUser, BigDecimal amount, String message, Boolean isActive) {
        this.initiatingUser = initiatingUser;
        this.amount = amount;
        this.message = message;
        this.isActive = isActive;
    }
}