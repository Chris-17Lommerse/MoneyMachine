package MoneyMachine.models.dtos.requests;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FilterRequest {
    private String toIban;
    private String fromIban;
    private BigDecimal minAmount;
    private BigDecimal maxAmount;
    private BigDecimal exactAmount;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
     private LocalDateTime exactDate;
    private Long userId;
}
