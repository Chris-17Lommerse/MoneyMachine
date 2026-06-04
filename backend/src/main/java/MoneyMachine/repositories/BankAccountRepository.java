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
import jakarta.transaction.Transactional;

@Repository
public interface BankAccountRepository extends JpaRepository<BankAccount, String> {
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT b FROM BankAccount b WHERE b.iban = :iban")
    Optional<BankAccount> findByIdForUpdate(String iban);
    public Page<BankAccount> findAllByUserId(Long id, Pageable pageable);
    public Optional<BankAccount> findByIbanAndUserId(String iban, Long id);
    @Modifying
    @Transactional
    @Query("UPDATE BankAccount SET balance = :newBalance WHERE iban = :iban")
    public void setBalanceByIban(@Param("iban") String iban, @Param("newBalance") BigDecimal newBalance);
}
