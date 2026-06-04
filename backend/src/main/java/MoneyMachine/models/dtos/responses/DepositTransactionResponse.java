package MoneyMachine.models.dtos.responses;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DepositTransactionResponse {

    private Long transactionId;
    private Long initiatingUserId;
    private String toAccountIban;
    private String message;
    private Double amount;
    private LocalDateTime dateTime;
    private Boolean isActive;
}
