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
public class TransferTransaction extends Transaction {

    @ManyToOne
    @NotNull
    private BankAccount fromBankAccount;
    @ManyToOne
    @NotNull
    private BankAccount toBankAccount;

    public TransferTransaction(User initiatingUser, BigDecimal amount, String message, boolean isActive, BankAccount fromBankAccount, BankAccount toBankAccount) {
        super(initiatingUser, amount, message, isActive); 
        this.fromBankAccount = fromBankAccount;
        this.toBankAccount = toBankAccount;
    }
}
