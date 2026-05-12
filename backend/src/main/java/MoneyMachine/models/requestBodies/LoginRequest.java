package MoneyMachine.models.requestBodies;

import MoneyMachine.models.enums.LoginType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    private String email;
    private String password;
    private LoginType loginType;
}
