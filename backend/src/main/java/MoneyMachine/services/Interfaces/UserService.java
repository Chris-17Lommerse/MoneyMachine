package MoneyMachine.services.interfaces;

import java.util.List;
import MoneyMachine.models.dtos.UserResponse;

import org.springframework.stereotype.Service;

@Service
public interface UserService {
    List<UserResponse> getAllUsersWithoutBankAccounts();
}
