package MoneyMachine.repositories;

import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import MoneyMachine.models.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.fromBankAccount.iban = :iban OR t.toBankAccount.iban = :iban")
    Page<Transaction> findAllByToOrFromIban(String iban,Pageable pageable);
    List<Transaction> findByFromIbanAndDateTimeGreaterThanEqualAndDateTimeLessThan(String fromIban,LocalDateTime start,LocalDateTime end);
}
