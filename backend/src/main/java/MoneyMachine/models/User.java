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
import lombok.Setter;

@Entity
@Table(name ="users")
public class User {
    @Id
    @GeneratedValue
    @Column(name = "user_id", length = 10, nullable = false, unique = true)
    @Getter
    @Setter
    private Long userId;

    @Column(name = "first_name", length = 100, nullable = false, unique = false)
    @Getter
    @Setter
    private String firstName;

    @Column(name = "last_name", length = 200, nullable = false, unique = false)
    @Getter
    @Setter
    private String lastName;

    @Column(name = "email", length = 300, nullable = false, unique = true)
    @Getter
    @Setter
    private String email;

    @Column(name = "bsn", length = 10, nullable = false, unique = true)
    @Getter
    @Setter
    private String bsn;

    @Column(name = "phone_number", length = 11, nullable = false, unique = true)
    @Getter
    @Setter
    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false, unique = true)
    @Getter
    @Setter
    private MoneyMachine.models.enums.Role role;

    @Column(name = "is_active", nullable = false, unique = false)
    @Getter
    @Setter
    private Boolean isActive;

    @Column(name = "is_apptoved", nullable = false, unique = false)
    @Getter
    @Setter
    private Boolean isApproved;

    public User() {}

    public User(Long userId, String firstName, String lastName, String email, String bsn, String phoneNumber, Role role, Boolean isActive, Boolean isApproved)
    {
        this.userId = userId;
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
