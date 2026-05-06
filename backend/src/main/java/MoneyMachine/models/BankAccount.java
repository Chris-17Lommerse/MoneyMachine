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

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private BigDecimal balance;

    @Column(nullable = false)
    private BigDecimal absoluteLimit;

    @Column(nullable = false)
    private BigDecimal singleTransferLimit;

    @Column(nullable = false)
    private BigDecimal dailyTransferLimit;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private BankAccountType bankAccountType;

    @Column(nullable = false)
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