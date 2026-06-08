package MoneyMachine.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import MoneyMachine.exception.NotAuthorizedException;
import MoneyMachine.mappers.UserMapper;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.models.dtos.responses.ITransactionResponse;
import MoneyMachine.models.dtos.responses.TransactionOverviewResponse;
import MoneyMachine.models.dtos.responses.UserResponse;
import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.models.enums.Role;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.interfaces.BankAccountService;
import MoneyMachine.services.interfaces.TransactionService;
import MoneyMachine.services.interfaces.UserService;
import jakarta.transaction.Transactional;
import java.util.Optional;
import MoneyMachine.exception.NotFoundException;

@Service
public class UserServiceImpl implements UserService {

    private UserMapper userMapper;
    private UserRepository userRepository;
    private BankAccountService bankAccountService;
    private TransactionService transactionService;

    public UserServiceImpl(UserRepository userRepository, UserMapper userMapper,
        BankAccountService bankAccountService, TransactionService transactionService) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
        this.bankAccountService = bankAccountService;
        this.transactionService = transactionService;
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

    public User getUserById(Long id) {
        return userRepository.findById(id).orElseThrow(()
            -> new NotFoundException(String.format("User with ID %s does not exist.", id))
        );
    }
    public TransactionOverviewResponse getTransactionsByUserId(@PathVariable Long id, Pageable pageable){
        BankAccountOverviewResponse bankAccountOverviewResponse = bankAccountService.getAllBankAccountsByUserId(id, pageable);
        List<BankAccountResponse> bankAccounts = bankAccountOverviewResponse.getItems();
        TransactionOverviewResponse transactions = new TransactionOverviewResponse(new ArrayList<>(), pageable.getPageNumber(), pageable.getPageSize());
        for(BankAccountResponse bankAccount:bankAccounts)
        {
            String iban = bankAccount.getIban();
            TransactionOverviewResponse overview=transactionService.getTransactionsByIban(iban,pageable);
            for (ITransactionResponse transactionResponse : overview.getTransactions()) {

                boolean isNewTransaction = true;

                for (ITransactionResponse existing : transactions.getTransactions()) {

                    if (Objects.equals(existing.getTransactionId(),transactionResponse.getTransactionId())) {

                        isNewTransaction = false;
                        break;
                    }
                }

                if (isNewTransaction) {
                    transactions.getTransactions().add(transactionResponse);
                }
            }
        }
        return transactions;
    }
}
