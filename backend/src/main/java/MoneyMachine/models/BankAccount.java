package MoneyMachine.models;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "bank_accounts")
@Data
@NoArgsConstructor
@AllArgsConstructor
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
    @NotNull
    private LocalDateTime createdAt;
}