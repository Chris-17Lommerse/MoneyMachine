package MoneyMachine.models.dtos.responses;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserSummaryResponse {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
}