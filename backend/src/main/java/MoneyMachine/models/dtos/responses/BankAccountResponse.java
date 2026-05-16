package MoneyMachine.models.dtos.responses;

import java.math.BigDecimal;
import MoneyMachine.models.*;
import MoneyMachine.models.enums.BankAccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountResponse {
    private String ibam;
    private User user;
    private BankAccountType bankAccountType;
    private BigDecimal balance;
    private BigDecimal singleTransferLimit;
    private BigDecimal dailyTransferLimit;
    private BigDecimal absoluteTransferLimit;
    private boolean isActive;
}
