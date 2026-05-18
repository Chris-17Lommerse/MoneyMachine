package MoneyMachine.services;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import MoneyMachine.exception.NotAuthorizedException;
import MoneyMachine.mappers.UserMapper;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.responses.UserResponse;
import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.models.enums.Role;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.interfaces.BankAccountService;
import MoneyMachine.services.interfaces.UserService;
import jakarta.transaction.Transactional;
import java.util.Optional;
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

    public List<UserResponse> getAllUsersWithoutBankAccounts() {

        Iterable<User> users = userRepository.findByBankAccountsIsEmpty();
        List<UserResponse> convertedUsers = new ArrayList<UserResponse>();

        for (User user : users) {
            convertedUsers.add(userMapper.toResponse(user));
        }

        return convertedUsers;
    }

    @Transactional
    public void approveUserAndCreateAccounts(Long userId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        User user = optionalUser.get();
        if (user == null) {
            throw new NotFoundException("User with user id" + userId + "Not found");
        }

        if (user.getIsActive() == false) {
            throw new NotAuthorizedException("User is not active");
        }

        if (user.getRole() == Role.USER) {
            throw new NotAuthorizedException("User is not allowed to create account");
        }

        for (BankAccountType bankAccountType : BankAccountType.values()) {
            bankAccountService.createBankAccountForUser(bankAccountType, user);
        }

        user.setIsApproved(true);
    }
}
