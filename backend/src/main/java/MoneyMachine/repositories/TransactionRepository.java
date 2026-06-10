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
    @Query("SELECT t FROM TransferTransaction t WHERE t.dateTime BETWEEN :start AND :end AND t.fromBankAccount.iban = :iban")
    List<Transaction> findTransactionsForIbanBetweentimes(String iban,LocalDateTime start,LocalDateTime end);
}
