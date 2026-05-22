package MoneyMachine.models.dtos.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositRequest  {
    @NotNull
    private String toAccount;
    @NotNull
    private Long transactionId;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private String type;
    @NotNull
    private long initiatedBy;
    @NotNull
    private String message;

    
}
