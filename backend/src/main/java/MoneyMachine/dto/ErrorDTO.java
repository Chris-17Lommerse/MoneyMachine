package MoneyMachine.dto;

import java.io.Serializable;

import javax.lang.model.type.ErrorType;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class ErrorDTO implements Serializable {
    @NotNull
    private int code;
    @Enumerated(EnumType.STRING)
    @NotNull
    private ErrorType type;
    @NotNull
    private String message;
    private String details;
}
