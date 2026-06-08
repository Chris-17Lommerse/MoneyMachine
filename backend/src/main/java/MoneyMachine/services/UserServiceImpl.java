package MoneyMachine.services;

import java.util.List;
import java.util.Objects;

import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;

import MoneyMachine.mappers.UserMapper;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.responses.UserOverviewResponse;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
import MoneyMachine.models.dtos.responses.ITransactionResponse;
import MoneyMachine.models.dtos.responses.TransactionOverviewResponse;
import MoneyMachine.models.dtos.responses.UserResponse;
import MoneyMachine.policies.ApprovingPolicy;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.interfaces.BankAccountService;
import MoneyMachine.services.interfaces.TransactionService;
import MoneyMachine.services.interfaces.UserService;
import jakarta.transaction.Transactional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    public UserOverviewResponse getAllUsersWithoutBankAccounts(Pageable pageable) {

        Page<User> page = userRepository.findByBankAccountsIsEmpty(pageable);
        List<User> users = page.getContent();
        List<UserResponse> items = userMapper.toResponseList(users);
        UserOverviewResponse userOverviewResponse = new UserOverviewResponse(items, page.getNumber(), page.getSize());

        return userOverviewResponse;
    }

    @Transactional(rollbackOn = Exception.class)
    public void approveUser(User user) {
        ApprovingPolicy approvingPolicy = new ApprovingPolicy();
        approvingPolicy.enforceApprovingPolicy(user);
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
