package MoneyMachine.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.models.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountCreatedResponse {
    private String ibam;
    private User user;
    private BankAccountType bankAccountType;
    private BigDecimal balance;
    private BigDecimal singleTransferLimit;
    private BigDecimal dailyTransferLimit;
    private BigDecimal absoluteTransferLimit;
    private boolean isActive;
    private LocalDateTime createdAt;
}
