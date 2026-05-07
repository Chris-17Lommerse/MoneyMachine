package MoneyMachine.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import MoneyMachine.models.BankAccount;

@Repository
public interface BankAccountRepository extends ListCrudRepository<BankAccount, String> {
    
}
