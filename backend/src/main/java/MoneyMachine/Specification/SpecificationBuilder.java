package MoneyMachine.Specification;

import java.time.LocalDateTime;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;
import MoneyMachine.models.Transaction;
import MoneyMachine.models.dtos.requests.FilterRequest;

@Component
public class SpecificationBuilder {
    private TransactionSpecification transactionSpecification;
    public SpecificationBuilder(TransactionSpecification transactionSpecification)
    {
        this.transactionSpecification=transactionSpecification;

    }

    public Specification<Transaction> build(FilterRequest filter) {

       Specification<Transaction> spec = (root, query, cb) -> cb.conjunction();

        if (filter.getFromIban() != null) {
            spec =spec.and( transactionSpecification.fromIban(filter.getFromIban()));
        }

        if (filter.getToIban() != null) {
            spec =spec.and( transactionSpecification.toIban(filter.getToIban()));
        }

        if (filter.getMinAmount() != null) {
            spec = spec.and( transactionSpecification.amountGreaterThan(filter.getMinAmount()));
        }

        if (filter.getMaxAmount() != null) {
            spec = spec.and( transactionSpecification.amountLessThan(filter.getMaxAmount()));
        }

        if (filter.getExactAmount() != null) {
            spec = spec.and( transactionSpecification.amountEquals(filter.getExactAmount()));
        }
        
        if (filter.getStartDate() != null) {
            spec = spec.and( transactionSpecification.dateAfter(filter.getStartDate()));
        }
        if (filter.getEndDate() != null) {
            spec = spec.and( transactionSpecification.dateBefore(filter.getEndDate()));
        }
        
        if (filter.getExactDate() != null) {
           
            LocalDateTime start = filter.getExactDate().toLocalDate().atStartOfDay();
            LocalDateTime end = start.plusDays(1).toLocalDate().atStartOfDay();
            spec = spec.and(transactionSpecification.dateBetween(start,end));
        }

        if (filter.getUserId() != null) {
            spec =spec.and( transactionSpecification.byUserId(filter.getUserId()));
        }

        return spec;
    }
    public Specification<Transaction> findByIban(String iban)
    {
        Specification<Transaction> spec = (root, query, cb) -> cb.conjunction();
        spec =spec.and( transactionSpecification.hasIban(iban));
        return spec;

    }

    
}
