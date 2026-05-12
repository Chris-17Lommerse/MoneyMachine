package MoneyMachine.models.dtos;

import MoneyMachine.models.enums.ErrorType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ErrorResponse {

    private Integer code;
    private ErrorType errorType;
    private String message;
    private String details;
}
