package MoneyMachine.models.dtos.responses;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonProperty;

import MoneyMachine.models.enums.BankAccountType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class BankAccountResponse {
    private String iban;
    private Long userId;
    private BankAccountType bankAccountType;
    private BigDecimal balance;
    private BigDecimal singleTransferLimit;
    private BigDecimal dailyTransferLimit;
    private BigDecimal absoluteLimit;
    @JsonProperty("isActive")
    private boolean isActive;
}
