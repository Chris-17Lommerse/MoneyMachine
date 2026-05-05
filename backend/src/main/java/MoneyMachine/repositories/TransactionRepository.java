package MoneyMachine.repositories;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import MoneyMachine.models.Transaction;

@Repository
public interface TransactionRepository extends ListCrudRepository<Transaction, Long> {
    
}
