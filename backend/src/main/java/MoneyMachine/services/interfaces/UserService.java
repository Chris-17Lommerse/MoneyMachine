package MoneyMachine.services.interfaces;

import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import MoneyMachine.models.User;
import MoneyMachine.models.dtos.responses.TransactionOverviewResponse;
import MoneyMachine.models.dtos.responses.UserResponse;

@Service
public interface UserService {
    List<UserResponse> getAllUsersWithoutBankAccounts();
    void approveUserAndCreateAccounts(Long userId);
    User getUserById(Long id);
    TransactionOverviewResponse getTransactionsByUserId(Long id, Pageable pageable);
}