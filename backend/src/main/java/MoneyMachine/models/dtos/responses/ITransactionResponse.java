package MoneyMachine.models.dtos.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public interface ITransactionResponse {
    Long getTransactionId();
    Long getInitiatingUserId();
    String getMessage();
    BigDecimal getAmount();
    LocalDateTime getDateTime();
    Boolean getIsActive();
    
}
