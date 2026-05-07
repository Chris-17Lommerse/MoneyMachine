package MoneyMachine.models;

import java.util.HashSet;

import MoneyMachine.models.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
public class User {

    @Id
    @GeneratedValue
    @NotNull
    private Long id;

    @Column(nullable = false, length = 50)
    @NotNull
    private String firstName;

    @Column(nullable = false, length = 50)
    @NotNull
    private String lastName;

    @Column(nullable = false, length = 255)
    @NotNull
    private String email;

    @Column(nullable = false, length = 9)
    @NotNull
    private String bsn;

    @Column(nullable = false, length = 30)
    @NotNull
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    @NotNull
    private Role role;

    private HashSet<BankAccount> bankAccounts;

    @Column(nullable = false)
    @NotNull
    private Boolean isActive;

    @Column(nullable = false)
    @NotNull
    private Boolean isApproved;

    public User(String firstName, String lastName, String email, String bsn, String phoneNumber, Role role, HashSet<BankAccount> bankAccounts, Boolean isActive, Boolean isApproved)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.bsn = bsn;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.bankAccounts = bankAccounts;
        this.isActive = isActive;
        this.isApproved = isApproved;
    }
}
