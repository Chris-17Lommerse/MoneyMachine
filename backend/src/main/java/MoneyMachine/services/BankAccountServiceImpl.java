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

        return new BankAccountOverviewResponse(bankAccountMapper.toResponseList(bankAccounts), page.getNumber(),
                page.getSize());
    }

    @Override
    public void setBankAccountBalance(String iban, BigDecimal newBalance) {
        this.bankAccountRepository.setBalanceByIban(iban, newBalance);
    }

    @Override
    public BankAccountResponse createBankAccountForUser(BankAccountType bankAccountType, User user) {

        BankAccount bankAccount = new BankAccount();

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

    @Override
    public BankAccountOverviewResponse getAllBankAccounts(Pageable pageable) {
        Page<BankAccount> page = bankAccountRepository.findAll(pageable);
        List<BankAccount> bankAccounts = page.getContent();
        List<BankAccountResponse> items = bankAccountMapper.toResponseList(bankAccounts);
        BankAccountOverviewResponse bankAccountOverviewResponse = new BankAccountOverviewResponse(items,
                page.getNumber(), page.getSize());
        return bankAccountOverviewResponse;
    }

    public BankAccountResponse closeBankAccount(PatchRequest patchRequest, String iban) {
        Optional<BankAccount> optionalBankAccount = bankAccountRepository.findById(iban);
        BankAccount bankAccount = optionalBankAccount.get();
        boolean isActive = patchRequest.isActive();
        bankAccount.setIsActive(isActive);

        bankAccountRepository.save(bankAccount);
        BankAccountResponse bankAccountResponse = bankAccountMapper.toResponse(bankAccount);
        return bankAccountResponse;
    }

    private String generateIBAN() {
        String generatedIban = ibanGenerator.generateIBAN();
        while (bankAccountRepository.existsById(generatedIban)) {
            generatedIban = ibanGenerator.generateIBAN();
        }
        return generatedIban;
    }

    @Override
    public BankAccountResponse getBankAccountByIban(String iban) {
        return bankAccountMapper.toResponse(findBankAccountEntityByIban(iban));
    }

    @Override
    public BankAccountResponse getBankAccountByIbanAndUserId(String iban, Long id) {
        BankAccount bankAccount = bankAccountRepository.findByIbanAndUserId(iban, id)
                .orElseThrow(() -> new NotFoundException(
                        String.format("Bank account with IBAN %s owned by %s does not exist.", iban, id)));

        return bankAccountMapper.toResponse(bankAccount);
    }

@Override
public BankAccount findBankAccountEntityByIban(String iban) {
    return bankAccountRepository.findById(iban).orElseThrow(() -> new NotFoundException(...));
}
}
