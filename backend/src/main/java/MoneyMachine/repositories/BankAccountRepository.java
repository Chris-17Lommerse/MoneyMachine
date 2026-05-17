package MoneyMachine.repositories;

import java.math.BigDecimal;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.ListCrudRepository;
import org.springframework.stereotype.Repository;

import MoneyMachine.models.BankAccount;

@Repository
public interface BankAccountRepository extends ListCrudRepository<BankAccount, String> {
    @Query("UPDATE BankAccount b SET b.balance = b.balance - ?2 WHERE b.iban = ?1")
    public void pay(String iban, BigDecimal amount);
    @Query("UPDATE BankAccount b SET b.balance = b.balance + ?2 WHERE b.iban = ?1")
    public void receive(String iban, BigDecimal amount);
}
