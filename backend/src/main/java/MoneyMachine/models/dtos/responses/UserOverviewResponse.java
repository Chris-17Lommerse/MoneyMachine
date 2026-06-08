package MoneyMachine.models.dtos.responses;

import java.io.Serializable;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserOverviewResponse implements Serializable {
    private List<UserResponse> items;
    private int page;
    private int pageSize;
}
