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
public class WithdrawTransaction extends Transaction {

    @ManyToOne
    @NotNull
    private BankAccount fromBankAccount;

    public WithdrawTransaction(User initiatingUser, BigDecimal amount, String message, boolean isActive, BankAccount fromBankAccount) {
        super(initiatingUser, amount, message, isActive); 
        this.fromBankAccount = fromBankAccount;
    }
}
