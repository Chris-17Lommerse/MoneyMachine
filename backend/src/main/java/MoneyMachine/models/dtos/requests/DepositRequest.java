package MoneyMachine.models.dtos.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositRequest {

    @NotNull
    private BigDecimal amount;
    @NotBlank
    private String toBankAcountIban;
}
