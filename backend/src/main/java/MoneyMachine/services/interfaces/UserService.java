package MoneyMachine.services.interfaces;

import MoneyMachine.models.dtos.responses.UserOverviewResponse;
import java.util.List;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import MoneyMachine.models.User;
import MoneyMachine.models.dtos.responses.TransactionOverviewResponse;
import MoneyMachine.models.dtos.responses.UserResponse;

public interface UserService {
    UserOverviewResponse getAllUsersWithoutBankAccounts(Pageable pageable);
    void approveUser(User user);
    List<UserResponse> getAllUsersWithoutBankAccounts();
    User getUserById(Long id);
    TransactionOverviewResponse getTransactionsByUserId(Long id, Pageable pageable);
}
