package MoneyMachine.models.dtos.requests;

import MoneyMachine.models.enums.LoginType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequest {

    @NotBlank
    private String email;
    @NotBlank
    private String password;
    @NotNull
    private LoginType loginType;
}
