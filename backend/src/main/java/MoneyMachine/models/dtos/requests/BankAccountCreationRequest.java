package MoneyMachine.models.dtos.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import MoneyMachine.models.enums.BankAccountType;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountCreationRequest {
    private Long userId;
    private BankAccountType bankAccountType;
    private BigDecimal balance;
    private BigDecimal singleTransferLimit;
    private BigDecimal dailyTransferLimit;
    private BigDecimal absoluteLimit;
}


