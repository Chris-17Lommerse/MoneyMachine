package MoneyMachine.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import MoneyMachine.models.Transaction;

@Repository
public interface TransactionRepository extends ListCrudRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.fromBankAccount.iban = :iban OR t.toBankAccount.iban = :iban")
   List<Transaction> findAllByToOrFromIban(String iban);
      
}
