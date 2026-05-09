package MoneyMachine.services.interfaces;

import java.util.List;
import MoneyMachine.models.dtos.UserDTO;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
    List<UserDTO> getAllUsersWithoutBankAccounts();
}
