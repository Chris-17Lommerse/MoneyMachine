package MoneyMachine.Specification;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.Transaction;
import MoneyMachine.models.TransferTransaction;
import MoneyMachine.models.WithdrawTransaction;
import jakarta.persistence.criteria.Root;
import lombok.Getter;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Component
public class TransactionSpecification {

    public Specification<Transaction> hasIban(String iban) {
        return (root, query, cb) -> {

            Root<TransferTransaction> transfer = cb.treat(root, TransferTransaction.class);
            Root<DepositTransaction> deposit = cb.treat(root, DepositTransaction.class);
            Root<WithdrawTransaction> withdraw = cb.treat(root, WithdrawTransaction.class);

            return cb.or
            (
                // transfer: from + to
                cb.or
                (
                    cb.equal(transfer.get("fromBankAccount").get("iban"), iban),
                    cb.equal(transfer.get("toBankAccount").get("iban"), iban)
                ),

                // deposit: only to
                cb.equal(deposit.get("toBankAccount").get("iban"), iban),

                // withdraw: only from
                cb.equal(withdraw.get("fromBankAccount").get("iban"), iban)
            );
        };
    }

    public Specification<Transaction> fromIban(String iban) {
        return (root, query, cb) -> {

            Root<TransferTransaction> transfer = cb.treat(root, TransferTransaction.class);
            Root<WithdrawTransaction> withdraw = cb.treat(root, WithdrawTransaction.class);

            return cb.or(
                cb.equal(transfer.get("fromBankAccount").get("iban"), iban),
                cb.equal(withdraw.get("fromBankAccount").get("iban"), iban)
            );
        };
    }

   public Specification<Transaction> toIban(String iban) {
        return (root, query, cb) -> {

            Root<TransferTransaction> transfer = cb.treat(root, TransferTransaction.class);
            Root<DepositTransaction> deposit = cb.treat(root, DepositTransaction.class);
           

            return cb.or(
                cb.equal(transfer.get("toBankAccount").get("iban"), iban),
                cb.equal(deposit.get("toBankAccount").get("iban"), iban)
            );
        };
    }

    public Specification<Transaction> amountGreaterThan(BigDecimal amount) {
        return (root, query, cb) ->
                cb.greaterThan(root.get("amount"), amount);
    }

    public Specification<Transaction> amountLessThan(BigDecimal amount) {
        return (root, query, cb) ->
                cb.lessThan(root.get("amount"), amount);
    }

    public Specification<Transaction> amountEquals(BigDecimal amount) {
        return (root, query, cb) ->
                cb.equal(root.get("amount"), amount);
    }

    public Specification<Transaction> dateAfter(LocalDateTime date) {
        return (root, query, cb) ->
                cb.greaterThan(root.get("dateTime"), date);
    }

    public Specification<Transaction> dateBefore(LocalDateTime date) {
        return (root, query, cb) ->
                cb.lessThan(root.get("dateTime"), date);
    }

    public Specification<Transaction> dateBetween(LocalDateTime start, LocalDateTime end) {
        return (root, query, cb) ->
                cb.between(root.get("dateTime"), start, end);
    }

    public Specification<Transaction> byUserId(long userId) {
        return (root, query, cb) ->
                cb.equal(root.get("initiatingUser").get("id"), userId);
    }
}
