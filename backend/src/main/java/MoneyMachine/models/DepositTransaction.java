package MoneyMachine.models;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
public class DepositTransaction extends Transaction {

    @ManyToOne
    @NotNull
    private BankAccount toBankAccount;

    public DepositTransaction(User initiatingUser, BigDecimal amount, String message, boolean isActive, BankAccount toBankAccount) {
        super(initiatingUser, amount, message, isActive); 
        this.toBankAccount = toBankAccount;
    }
}
