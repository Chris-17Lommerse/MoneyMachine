package MoneyMachine.repositories;

import java.util.List;

import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import MoneyMachine.models.WithdrawTransaction;

@Repository
public interface WithdrawTransactionRepository extends ListCrudRepository<WithdrawTransaction, Long> {
    List<WithdrawTransaction> findByFromBankAccount_Iban(String iban);
}
