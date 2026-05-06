package MoneyMachine.models;

import java.math.BigDecimal;

import MoneyMachine.models.enums.BankAccountType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "bank_accounts")
@Getter
@Setter
@NoArgsConstructor
public class BankAccount {

    @Id
    private String iban;
    private Long userId;
    private BigDecimal balance;
    private BigDecimal absoluteLimit;
    private BigDecimal singleTransferLimit;
    private BigDecimal dailyTransferLimit;
    @Enumerated(EnumType.STRING)
    private BankAccountType bankAccountType;
    private Boolean isActive;

    public BankAccount(String iban, Long userId, BigDecimal balance, BigDecimal absoluteLimit, BigDecimal singleTransferLimit, BigDecimal dailyTransferLimit, BankAccountType bankAccountType) {
        this.iban = iban;
        this.userId = userId;
        this.balance = balance;
        this.absoluteLimit = absoluteLimit;
        this.singleTransferLimit = singleTransferLimit;
        this.dailyTransferLimit = dailyTransferLimit;
        this.bankAccountType = bankAccountType;
    }
}