package MoneyMachine.models.dtos.requests;

import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawRequest {

    private BigDecimal amount;
    private String fromBankAcountIban;
}
