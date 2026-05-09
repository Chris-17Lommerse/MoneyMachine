package MoneyMachine.models.dtos;

import java.io.Serializable;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class UserOverviewDTO implements Serializable {
    private List<UserDTO> users;
}
