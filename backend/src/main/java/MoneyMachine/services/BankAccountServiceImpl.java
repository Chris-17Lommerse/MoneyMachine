package MoneyMachine.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import MoneyMachine.exception.NotFoundException;
import org.springframework.stereotype.Service;

import MoneyMachine.models.BankAccount;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.services.interfaces.BankAccountService;

@Service
public class BankAccountServiceImpl implements BankAccountService {

    private BankAccountRepository bankAccountRepository;

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    @Override
    public List<BankAccount> findBankAccountsByUserId(Long id) {
        return bankAccountRepository.findAllByUserId(id);
    }

    @Override
    public BankAccount getBankAccountByIban(String iban) {
        
        Optional<BankAccount> bankAccount = bankAccountRepository.findById(iban);

        if (bankAccount.isPresent()) {
            return bankAccount.get();
        }

        throw new NotFoundException(String.format("Bank account with IBAN %s does not exist.", iban));
    } 

    @Override
    public BankAccount getBankAccountByIbanAndUserId(String iban, Long id) {
        
        Optional<BankAccount> bankAccount = bankAccountRepository.findByIbanAndUserId(iban, id);

        if (bankAccount.isPresent()) {
            return bankAccount.get();
        }

        throw new NotFoundException(String.format("Bank account with IBAN %s owned by %s does not exist.", iban, id));
    }

    @Override
    public void setBankAccountBalance(String iban, BigDecimal newBalance) {
        this.bankAccountRepository.setBalanceByIban(iban, newBalance);
    } 
}
