package MoneyMachine.models;

import MoneyMachine.models.enums.Role;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String bsn;

    @Column(nullable = false)
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;

    @Column(nullable = false)
    private Boolean isActive;

    @Column(nullable = false)
    private Boolean isApproved;

    public User(String firstName, String lastName, String email, String bsn, String phoneNumber, Role role, Boolean isActive, Boolean isApproved)
    {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.bsn = bsn;
        this.phoneNumber = phoneNumber;
        this.role = role;
        this.isActive = isActive;
        this.isApproved = isApproved;
    }
}