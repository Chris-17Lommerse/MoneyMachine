package MoneyMachine.models.dtos;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserOverviewResponse implements Serializable {
    private List<UserResponse> users;
}
