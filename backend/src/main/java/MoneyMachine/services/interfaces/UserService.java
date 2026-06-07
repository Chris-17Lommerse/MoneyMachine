package MoneyMachine.services.interfaces;
import org.springframework.stereotype.Service;

import MoneyMachine.models.dtos.responses.UserOverviewResponse;
import org.springframework.data.domain.Pageable;

@Service
public interface UserService {
    UserOverviewResponse getAllUsersWithoutBankAccounts(Pageable pageable);
    void approveUserAndCreateAccounts(Long userId);
}
