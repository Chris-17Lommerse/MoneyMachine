package MoneyMachine.models.dtos;

import MoneyMachine.models.enums.Role;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Long userId;
    private String firstName;
    private String lastName;
    private String email;
    private String bsn;
    private String phoneNumber;
    private Role role;
    private Boolean isActive;
    private Boolean isApproved;
}