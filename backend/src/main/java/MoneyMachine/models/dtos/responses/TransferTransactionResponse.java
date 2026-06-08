package MoneyMachine.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransferTransactionResponse implements ITransactionResponse , Serializable  {

    private Long transactionId;
    private Long initiatingUserId;
    private String toAccountIban;
    private String fromAccountIban;
    private String message;
    private BigDecimal amount;
    private LocalDateTime dateTime;
    private Boolean isActive;
}


   
