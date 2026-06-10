package MoneyMachine.services;

import java.util.List;

import org.springframework.stereotype.Service;

import MoneyMachine.mappers.UserMapper;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.responses.UserOverviewResponse;
import MoneyMachine.models.dtos.responses.UserResponse;
import MoneyMachine.policies.ApprovingPolicy;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.interfaces.UserService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import MoneyMachine.exception.NotFoundException;

@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;
    private UserRepository userRepository;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }
    
    @Override
    public UserOverviewResponse getAllUsersWithoutBankAccounts(Pageable pageable) {

        Page<User> page = userRepository.findByBankAccountsIsEmpty(pageable);
        List<User> users = page.getContent();
        List<UserResponse> items = userMapper.toResponseList(users);
        UserOverviewResponse userOverviewResponse = new UserOverviewResponse(items, page.getNumber(), page.getSize());

        return userOverviewResponse;
    }

    @Override
    @Transactional(rollbackOn = Exception.class)
    public void approveUser(User user) {
        ApprovingPolicy approvingPolicy = new ApprovingPolicy();
        approvingPolicy.enforceApprovingPolicy(user);
    }

    @Override
    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()
            -> new NotFoundException(String.format("User with ID %s does not exist.", id))
        );
    }
}
