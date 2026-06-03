package MoneyMachine.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import MoneyMachine.exception.NotFoundException;
import org.springframework.stereotype.Service;

import MoneyMachine.models.BankAccount;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.services.interfaces.BankAccountService;

import MoneyMachine.mappers.BankAccountMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.repositories.UserRepository;

import MoneyMachine.strategies.interfaces.BankAccountTypeStrategy;
import MoneyMachine.factories.BankAccountTypeFactory;
import MoneyMachine.factories.IbanGenerator;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.requests.BankAccountCreationRequest;
import MoneyMachine.models.dtos.requests.PatchRequest;
import MoneyMachine.models.dtos.responses.BankAccountOverviewResponse;
import MoneyMachine.models.dtos.responses.BankAccountResponse;

import java.time.LocalDateTime;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private BankAccountMapper bankAccountMapper;
    private BankAccountRepository bankAccountRepository;
    private IbanGenerator ibanGenerator;
    private BankAccountTypeFactory bankAccountTypeFactory;
    private UserRepository userRepository;
    // private static final BigDecimal balance = BigDecimal.valueOf(0);
    // private static final BigDecimal absoluteLimit = BigDecimal.valueOf(0);
    // private static final BigDecimal dailyTransferLimit = BigDecimal.valueOf(20000);
    // private static final BigDecimal singleTransferLimit = BigDecimal.valueOf(5000);

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, UserRepository userRepository,
            BankAccountMapper bankAccountMapper, IbanGenerator ibanGenerator,
            BankAccountTypeFactory bankAccountTypeFactory) {
        this.bankAccountRepository = bankAccountRepository;
        this.bankAccountMapper = bankAccountMapper;
        this.ibanGenerator = ibanGenerator;
        this.bankAccountTypeFactory = bankAccountTypeFactory;
        this.userRepository = userRepository;
    }

    @Override
    public BankAccountOverviewResponse getAllBankAccountsByUserId(Long id, Pageable pageable) {

        Page<BankAccount> page = bankAccountRepository.findAllByUserId(id, pageable);
        List<BankAccount> bankAccounts = page.getContent();

        return new BankAccountOverviewResponse(bankAccountMapper.toResponseList(bankAccounts), page.getNumber(), page.getSize());
    }

    @Override
    public BankAccountResponse getBankAccountByIban(String iban) {

        Optional<BankAccount> bankAccount = bankAccountRepository.findById(iban);
            
        if (bankAccount.isPresent()) {
            return bankAccountMapper.toResponse(bankAccount.get()); 
        }

        throw new NotFoundException(String.format("Bank account with IBAN %s does not exist.", iban));
    }

    @Override
    public BankAccountResponse getBankAccountByIbanAndUserId(String iban, Long id) {
        
        Optional<BankAccount> bankAccount = bankAccountRepository.findByIbanAndUserId(iban, id);
            
        if (bankAccount.isPresent()) {
            return bankAccountMapper.toResponse(bankAccount.get()); 
        }

        throw new NotFoundException(String.format("Bank account with IBAN %s owned by %s does not exist.", iban, id));
    }

    @Override
    public BankAccount getBankAccountEntityByIban(String iban) {
        return bankAccountRepository.findById(iban).orElseThrow(() 
            -> new NotFoundException(String.format("Bank account with IBAN %s does not exist.", iban))
        );
    }

    @Override
    public void setBankAccountBalance(String iban, BigDecimal newBalance) {
        this.bankAccountRepository.setBalanceByIban(iban, newBalance);
    }

    @Override
    public BankAccountResponse createBankAccountForUser(BankAccountType bankAccountType, User user) {

        BankAccount bankAccount = new BankAccount();

        String iban = generateIban();
        // BankAccount bankAccount = new BankAccount(iban, user, balance, absoluteLimit, singleTransferLimit, dailyTransferLimit, bankAccountType, true, LocalDateTime.now());
        BankAccountTypeStrategy strategy = bankAccountTypeFactory.getStrategy(bankAccountType);
        strategy.applyBankAccountRules(bankAccount);
        bankAccountRepository.save(bankAccount);
        BankAccountResponse bankAccountRespnse = bankAccountMapper.toResponse(bankAccount);
        return bankAccountRespnse;
    }

    @Override
    public BankAccountResponse createBankAccountFromRequest(BankAccountCreationRequest bankAccountCreationRequest) {
        Optional<User> optionalUser = userRepository.findById(bankAccountCreationRequest.getUserId());
        User user = optionalUser.get();

        String iban = generateIban();
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

    @Override
    public BankAccountOverviewResponse getAllBankAccounts(Pageable pageable) {
        Page<BankAccount> page = bankAccountRepository.findAll(pageable);
        List<BankAccount> bankAccounts = page.getContent();
        List<BankAccountResponse> items = bankAccountMapper.toResponseList(bankAccounts);
        BankAccountOverviewResponse bankAccountOverviewResponse = new BankAccountOverviewResponse(items,
                page.getNumber(), page.getSize());
        return bankAccountOverviewResponse;
    }

    @Override
    public BankAccountResponse closeBankAccount(PatchRequest patchRequest, String iban) {
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(iban);
        BankAccount bankAccount = optionalBankAccount.get();
        boolean isActive = patchRequest.isActive();
        bankAccount.setIsActive(isActive);

        bankAccountRepository.save(bankAccount);
        BankAccountResponse bankAccountResponse = bankAccountMapper.toResponse(bankAccount);
        return bankAccountResponse;
    }
  
    private String generateIban() {
        String generatedIban = ibanGenerator.generateIban();
        while (bankAccountRepository.existsById(generatedIban)) {
            generatedIban = ibanGenerator.generateIban();
        }
        return generatedIban;
    }
}
