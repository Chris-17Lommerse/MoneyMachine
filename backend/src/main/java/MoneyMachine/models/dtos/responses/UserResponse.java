package MoneyMachine.models.dtos.responses;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonProperty;

import MoneyMachine.models.enums.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse implements Serializable {
    @NotNull
    private Long userId;
    @NotNull
    private String firstName;
    @NotNull
    private String lastName;
    @NotNull
    private String email;
    @NotNull
    private String bsn;
    @NotNull
    private String phoneNumber;
    @NotNull
    @Enumerated(EnumType.STRING)
    private Role role;
    @NotNull
    @JsonProperty("isActive")
    private boolean isActive;
    @NotNull
    @JsonProperty("isApproved")
    private boolean isApproved;
}
