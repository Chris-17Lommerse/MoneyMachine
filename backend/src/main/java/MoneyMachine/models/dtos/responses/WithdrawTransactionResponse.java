package MoneyMachine.models.dtos.responses;

import java.time.ZonedDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WithdrawTransactionResponse {

    private Long transactionId;
    private Long initiatingUserId;
    private String fromAccountIban;
    private String message;
    private Double amount;
    private ZonedDateTime dateTime;
    private Boolean isActive;
}
