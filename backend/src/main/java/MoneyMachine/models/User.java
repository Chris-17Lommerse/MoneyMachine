package MoneyMachine.models;

import java.util.HashSet;
import java.util.Set;

import MoneyMachine.models.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
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

    @OneToMany(mappedBy = "user")
    private Set<BankAccount> bankAccounts = new HashSet<BankAccount>(0);

    @Column(nullable = false)
    @NotNull
    private String password;

    @Column(nullable = false)
    @NotNull
    private Boolean isActive;

    @Column(nullable = false)
    @NotNull
    private Boolean isApproved;
}
