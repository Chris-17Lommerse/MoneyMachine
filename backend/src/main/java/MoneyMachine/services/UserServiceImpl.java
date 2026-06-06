package MoneyMachine.services;

import java.util.List;

import org.springframework.stereotype.Service;

import MoneyMachine.mappers.UserMapper;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.responses.UserOverviewResponse;
import MoneyMachine.models.dtos.responses.UserResponse;
import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.models.policies.ApprovingPolicy;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.interfaces.BankAccountService;
import MoneyMachine.services.interfaces.UserService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import MoneyMachine.exception.NotFoundException;

@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;
    private UserRepository userRepository;
    private BankAccountService bankAccountService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
            BankAccountService bankAccountService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.bankAccountService = bankAccountService;
    }

    public UserOverviewResponse getAllUsersWithoutBankAccounts(Pageable pageable) {

        Page<User> page = userRepository.findByBankAccountsIsEmpty(pageable);
        List<User> users = page.getContent();
        List<UserResponse> items = userMapper.toResponseList(users);
        UserOverviewResponse userOverviewResponse = new UserOverviewResponse(items, page.getNumber(), page.getSize());

        return userOverviewResponse;
    }

    @Transactional(rollbackOn = Exception.class)
    public void approveUserAndCreateAccounts(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.get();

        ApprovingPolicy approvingPolicy = new ApprovingPolicy();
        approvingPolicy.enforceUserIsNotNull(user);
        approvingPolicy.enforceUserIsNotActive(user);
        approvingPolicy.enforceUserIsNotAuthorizedToCreateAccount(user);

        for (BankAccountType bankAccountType : BankAccountType.values()) {
            bankAccountService.createBankAccountForUser(bankAccountType, user);
        }

        user.setIsApproved(true);
    }

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()
            -> new NotFoundException(String.format("User with ID %s does not exist.", id))
        );
    }
}
