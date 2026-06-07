package MoneyMachine.models.dtos.requests;

import java.math.BigDecimal;


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
    @NotNull
    private String fromBankAcountIban;
    @NotNull
    private String toBankAcountIban;
    @NotNull
    private String message;
}
