package MoneyMachine.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import MoneyMachine.models.DepositTransaction;

@Repository
public interface DepositTransactionRepository extends ListCrudRepository<DepositTransaction, Long> {
    List<DepositTransaction> findByToBankAccount_Iban(String iban); 
}
