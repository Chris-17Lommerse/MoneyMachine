package MoneyMachine.services;

import MoneyMachine.mappers.BankAccountMapper;
import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.springframework.stereotype.Service;

import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.models.enums.Role;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.interfaces.BankAccountService;
import MoneyMachine.strategies.CheckingStrategy;
import MoneyMachine.strategies.SavingsStrategy;
import MoneyMachine.strategies.interfaces.BankAccountTypeStrategy;
import MoneyMachine.exception.NotAuthorizedException;
import MoneyMachine.exception.NotFoundException;
import MoneyMachine.factories.BankAccountTypeFactory;
import MoneyMachine.factories.IbanGenerator;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.requests.BankAccountCreationRequest;
import MoneyMachine.models.dtos.responses.BankAccountResponse;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountMapper bankAccountMapper;
    private BankAccountRepository bankAccountRepository;
    private UserRepository userRepository;
    private IbanGenerator ibanGenerator;
    private BankAccountTypeFactory bankAccountTypeFactory;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, UserRepository userRepository,
            BankAccountMapper bankAccountMapper, IbanGenerator ibanGenerator, BankAccountTypeFactory bankAccountTypeFactory) {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
        this.bankAccountMapper = bankAccountMapper;
        this.ibanGenerator = ibanGenerator;
        this.bankAccountTypeFactory = bankAccountTypeFactory;
    }

    public BankAccountResponse createBankAccount(BankAccountCreationRequest bankAccountCreationRequest) {
        User user = userRepository.findById(bankAccountCreationRequest.getUserId());

        if (user == null) {
            throw new NotFoundException("User with user id" + bankAccountCreationRequest.getUserId() + "Not found");
        }

        if (user.getIsActive() == false) {
            throw new NotAuthorizedException("User is not active");
        }

        if (user.getRole() == Role.USER) {
            throw new NotAuthorizedException("User is not allowed to create account");
        }

        String generatedIban = ibanGenerator.generateIBAN();
        while (bankAccountRepository.existsById(generatedIban)) {
            generatedIban = ibanGenerator.generateIBAN();
        }
        BankAccount bankAccount = new BankAccount(generatedIban, user, bankAccountCreationRequest.getBalance(),
                bankAccountCreationRequest.getAbsoluteLimit(), bankAccountCreationRequest.getSingleTransferLimit(),
                bankAccountCreationRequest.getDailyTransferLimit(), bankAccountCreationRequest.getBankAccountType(),
                true, LocalDateTime.now());
        
        BankAccountTypeStrategy strategy = bankAccountTypeFactory.getStrategy(bankAccountCreationRequest.getBankAccountType());
        strategy.applyBankAccountRules(bankAccount);
        
        bankAccountRepository.save(bankAccount);
        BankAccountResponse bankAccountRespnse = bankAccountMapper.toResponse(bankAccount);
        return bankAccountRespnse;
    }
}
