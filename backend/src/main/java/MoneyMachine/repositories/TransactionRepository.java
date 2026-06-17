package MoneyMachine.repositories;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.math.BigDecimal;
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
    Page<Transaction> findAllByToOrFromIban(@Param("iban")String iban,Pageable pageable);
    @Query("SELECT t FROM TransferTransaction t WHERE t.dateTime BETWEEN :start AND :end AND t.fromBankAccount.iban = :iban")
    List<Transaction> findTransactionsForIbanBetweentimes(@Param("iban")String iban,@Param("start")LocalDateTime start,@Param("end")LocalDateTime end);
    @Query("SELECT t FROM Transaction t WHERE t.fromBankAccount.iban = :iban")
    Page<Transaction> getTransactionsByFromIban(@Param("iban")String filterValue, Pageable pageable);
    @Query("SELECT t FROM Transaction t WHERE t.toBankAccount.iban = :iban")
    Page<Transaction> getTransactionsByToIban(@Param("iban")String filterValue, Pageable pageable);
    @Query("SELECT t FROM Transaction t WHERE t.amount > :amount")
    Page<Transaction> getTransactionsByHigherThanAmount(@Param("amount")BigDecimal bigDecimal, Pageable pageable);
    @Query("SELECT t FROM Transaction t WHERE t.amount < :amount")
    Page<Transaction> getTransactionsByLowerThanAmount(@Param("amount")BigDecimal bigDecimal, Pageable pageable);
    @Query("SELECT t FROM Transaction t WHERE t.amount = :amount")
    Page<Transaction> getTransactionsByAmount(@Param("amount")BigDecimal bigDecimal, Pageable pageable);
    @Query("SELECT t FROM Transaction t WHERE t.dateTime > :date")
    Page<Transaction> getTransactionsByDateAfter(@Param("date")LocalDateTime filterValue, Pageable pageable);
    @Query("SELECT t FROM Transaction t WHERE t.dateTime < :date")
    Page<Transaction> getTransactionsByDateBefore(@Param("date")LocalDateTime filterValue, Pageable pageable);
    @Query("SELECT t FROM Transaction t WHERE t.dateTime >= :start AND t.dateTime < :end")
    Page<Transaction> getTransactionsByDate(@Param("start") LocalDateTime start,@Param("end") LocalDateTime end,Pageable pageable);
    @Query("SELECT t FROM Transaction t WHERE t.initiatingUser.id = :userId")
    Page<Transaction> getTransactionsByinitiatingUserId(@Param("userId")long userId, Pageable pageable);
    @Query("SELECT SUM(amount) FROM TransferTransaction t WHERE t.dateTime BETWEEN :start AND :end AND t.fromBankAccount.iban = :iban")
    BigDecimal findSpendAmountForIbanBetweentimes(@Param("iban")String iban, @Param("start")LocalDateTime start,@Param("end")LocalDateTime end);
}
