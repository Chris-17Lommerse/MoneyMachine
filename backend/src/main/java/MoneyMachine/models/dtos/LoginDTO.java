package MoneyMachine.models.dtos;

import MoneyMachine.models.enums.LoginType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginDTO {
    
    private String jwt;
    private LoginType loginType;
}
