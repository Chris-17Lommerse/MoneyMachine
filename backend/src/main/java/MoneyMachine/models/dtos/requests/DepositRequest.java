package MoneyMachine.models.dtos.requests;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositRequest {

    private BigDecimal amount;
    private String toBankAcountIban;
    private String message;
}
