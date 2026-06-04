package MoneyMachine.models;

import java.math.BigDecimal;

import MoneyMachine.models.enums.BankAccountType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
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
    @NotNull
    @Column(length = 34)
    private String iban;

    @ManyToOne
    @JoinColumn(nullable = false)
    @NotNull
    private User user;

    @Column(nullable = false)
    @NotNull
    private BigDecimal balance;

    @Column(nullable = false)
    @NotNull
    private BigDecimal absoluteLimit;

    @Column(nullable = false)
    @NotNull
    private BigDecimal singleTransferLimit;

    @Column(nullable = false)
    @NotNull
    private BigDecimal dailyTransferLimit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private BankAccountType bankAccountType;

    @Column(nullable = false)
    @NotNull
    private Boolean isActive;

    public BankAccount(String iban, User user, BigDecimal balance, BigDecimal absoluteLimit, BigDecimal singleTransferLimit, BigDecimal dailyTransferLimit, BankAccountType bankAccountType, Boolean isActive) {
        this.iban = iban;
        this.user = user;
        this.balance = balance;
        this.absoluteLimit = absoluteLimit;
        this.singleTransferLimit = singleTransferLimit;
        this.dailyTransferLimit = dailyTransferLimit;
        this.bankAccountType = bankAccountType;
        this.isActive = isActive;
    }
}