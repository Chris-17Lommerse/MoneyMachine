package MoneyMachine.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import MoneyMachine.models.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long> {
    @Query("SELECT t FROM Transaction t WHERE t.fromBankAccount.iban = :iban OR t.toBankAccount.iban = :iban")
    Page<Transaction> findAllByToOrFromIban(String iban,Pageable pageable);
    @Query("SELECT SUM(amount) FROM TransferTransaction t WHERE t.dateTime BETWEEN :start AND :end AND t.fromBankAccount.iban = :iban")
    BigDecimal findSpendAmountForIbanBetweentimes(@Param("iban")String iban, @Param("start")LocalDateTime start,@Param("end")LocalDateTime end);
}
