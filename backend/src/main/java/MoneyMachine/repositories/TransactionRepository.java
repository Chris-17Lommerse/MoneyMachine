package MoneyMachine.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;
import MoneyMachine.models.Transaction;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Long>,JpaSpecificationExecutor<Transaction> {
    @Query("SELECT t FROM TransferTransaction t WHERE t.dateTime BETWEEN :start AND :end AND t.fromBankAccount.iban = :iban")
    List<Transaction> findTransactionsForIbanBetweentimes(@Param("iban")String iban,@Param("start")LocalDateTime start,@Param("end")LocalDateTime end);
    @Query("SELECT SUM(amount) FROM TransferTransaction t WHERE t.dateTime BETWEEN :start AND :end AND t.fromBankAccount.iban = :iban")
    BigDecimal findSpendAmountForIbanBetweentimes(@Param("iban")String iban, @Param("start")LocalDateTime start,@Param("end")LocalDateTime end);
}
