package MoneyMachine.models.dtos.responses;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;



@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionResponse implements Serializable {
    @NotNull
    private Long transactionId;
    @NotNull
    private BigDecimal amount;
    @NotNull
    private LocalDateTime time;
    @NotNull
    private String type;
    @NotNull
    private String initiatedBy;
    @NotNull
    private String message;
    String fromAccount;
    String toAccount;
    
    
}

   
