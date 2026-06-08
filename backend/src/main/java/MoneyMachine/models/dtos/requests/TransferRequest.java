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
public class TransferRequest  {
    
    @NotNull
    private BigDecimal amount;
    @NotBlank
    private String fromBankAcountIban;
    @NotBlank
    private String toBankAcountIban;
    @NotBlank
    private String message;
}
