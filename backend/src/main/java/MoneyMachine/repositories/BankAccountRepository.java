package MoneyMachine.repositories;

import java.math.BigDecimal;
import java.util.Optional;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.stereotype.Repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import MoneyMachine.models.BankAccount;
import jakarta.persistence.LockModeType;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM BankAccount b WHERE b.iban = :iban")
    Optional<BankAccount> findByIdForUpdate(String iban);
    Page<BankAccount> findAllByUserId(Long id, Pageable pageable);
    Optional<BankAccount> findByIbanAndUserId(String iban, Long id);
    @Modifying
    @Query("UPDATE BankAccount SET balance = balance - :amount WHERE iban = :iban")
    void decrementBalanceByIban(@Param("iban") String iban, @Param("amount") BigDecimal amount);
    @Modifying
    @Query("UPDATE BankAccount b SET b.balance = b.balance + :amount WHERE b.iban = :iban")
    void incrementBalanceByIban(@Param("iban") String iban, @Param("amount") BigDecimal amount);
}
