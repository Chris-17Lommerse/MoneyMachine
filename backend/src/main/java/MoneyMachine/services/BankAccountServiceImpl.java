package MoneyMachine.services;

import MoneyMachine.mappers.BankAccountMapper;
import org.springframework.stereotype.Service;

import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.interfaces.BankAccountService;

import MoneyMachine.strategies.interfaces.BankAccountTypeStrategy;
import MoneyMachine.factories.BankAccountTypeFactory;
import MoneyMachine.factories.IbanGenerator;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.requests.BankAccountCreationRequest;
import MoneyMachine.models.dtos.responses.BankAccountResponse;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountMapper bankAccountMapper;
    private BankAccountRepository bankAccountRepository;
    private IbanGenerator ibanGenerator;
    private BankAccountTypeFactory bankAccountTypeFactory;
    private UserRepository userRepository;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, UserRepository userRepository,
            BankAccountMapper bankAccountMapper, IbanGenerator ibanGenerator,
            BankAccountTypeFactory bankAccountTypeFactory) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
        this.ibanGenerator = ibanGenerator;
        this.bankAccountTypeFactory = bankAccountTypeFactory;
        this.userRepository = userRepository;
    }

    public BankAccountResponse createBankAccountForUser(BankAccountType bankAccountType, User user) {

        String iban = generateIBAN();
        BankAccount bankAccount = new BankAccount(iban, user, 500, 500, 500, 500, bankAccountType, true, LocalDateTime.now());

        BankAccountTypeStrategy strategy = bankAccountTypeFactory.getStrategy(bankAccountType);
        strategy.applyBankAccountRules(bankAccount);

        bankAccountRepository.save(bankAccount);
        BankAccountResponse bankAccountRespnse = bankAccountMapper.toResponse(bankAccount);
        return bankAccountRespnse;
    }

    public BankAccountResponse createBankAccountFromRequest(BankAccountCreationRequest bankAccountCreationRequest) {
        Optional<User> optionalUser = userRepository.findById(bankAccountCreationRequest.getUserId());
        User user = optionalUser.get();

        String iban = generateIBAN();
        BankAccount bankAccount = new BankAccount(iban, user, bankAccountCreationRequest.getBalance(),
                bankAccountCreationRequest.getAbsoluteLimit(), bankAccountCreationRequest.getSingleTransferLimit(),
                bankAccountCreationRequest.getDailyTransferLimit(), bankAccountCreationRequest.getBankAccountType(),
                true, LocalDateTime.now());
        BankAccountTypeStrategy strategy = bankAccountTypeFactory
                .getStrategy(bankAccountCreationRequest.getBankAccountType());
        strategy.applyBankAccountRules(bankAccount);
        bankAccountRepository.save(bankAccount);
        BankAccountResponse bankAccountRespnse = bankAccountMapper.toResponse(bankAccount);
        return bankAccountRespnse;
    }

    private String generateIBAN() {
        String generatedIban = ibanGenerator.generateIBAN();
        while (bankAccountRepository.existsById(generatedIban)) {
            generatedIban = ibanGenerator.generateIBAN();
        }
        return generatedIban;
    }
}
