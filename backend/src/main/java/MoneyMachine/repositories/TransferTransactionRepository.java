package MoneyMachine.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;
import MoneyMachine.models.TransferTransaction;

@Repository
public interface TransferTransactionRepository extends ListCrudRepository<TransferTransaction, Long> {
    List<TransferTransaction> findByFromBankAccount_Iban(String iban);
    List<TransferTransaction> findByToBankAccount_Iban(String iban);
}