package MoneyMachine.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MoneyMachine.models.BankAccount;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
}
