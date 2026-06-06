package MoneyMachine.services.interfaces;

import java.util.List;

import org.springframework.stereotype.Service;

import MoneyMachine.models.dtos.responses.UserResponse;

@Service
public interface UserService {
    List<UserResponse> getAllUsersWithoutBankAccounts();
    void approveUserAndCreateAccounts(Long userId);
}