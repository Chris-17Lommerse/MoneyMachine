package MoneyMachine.repositories;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import MoneyMachine.models.BankAccount;
import jakarta.transaction.Transactional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    public List<BankAccount> findAllByUserId(Long id);
    public Optional<BankAccount> findByIbanAndUserId(String iban, Long id);
    @Modifying
    @Transactional
    @Query("UPDATE BankAccount SET balance = :newBalance WHERE iban = :iban")
    public void setBalanceByIban(@Param("iban") String iban, @Param("newBalance") BigDecimal newBalance);
}
