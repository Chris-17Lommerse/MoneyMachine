package MoneyMachine.services;

import java.util.List;

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
}
