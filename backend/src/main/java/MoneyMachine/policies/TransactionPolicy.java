package MoneyMachine.policies;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import MoneyMachine.exception.NotAuthorizedException;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.User;
import MoneyMachine.models.enums.Role;

@Component
public class TransactionPolicy {

    public void enforceTransactionPolicy(User user, BigDecimal amount, BankAccount fromBankAccount, BankAccount toBankAccount) {

        enforcePositiveAmount(amount);

        if (fromBankAccount != null && toBankAccount != null) {
            enforceNotSameAccountTransfer(fromBankAccount, toBankAccount);
            enforceNotDiffrentUserSavingsTransfer(fromBankAccount, toBankAccount);
        }

        if (fromBankAccount != null) {

            if (user.getRole() == Role.USER) {
                enforceUserOwnsFromAccount(user, fromBankAccount);
            }

            enforceWithinSingleTransferLimit(fromBankAccount, amount);
            enforceAbsoluteLimit(fromBankAccount, amount);
        }
        else if (toBankAccount != null) {

            if (user.getRole() == Role.USER) {
                enforceUserOwnsFromAccount(user, toBankAccount);
            }

            enforceWithinSingleTransferLimit(toBankAccount, amount);
        }        
    }

    private void enforceUserOwnsFromAccount(User user, BankAccount fromAccount) {
        if (fromAccount.getUser().getId() != user.getId()) {
            throw new NotAuthorizedException("Users can only transfer from their own accounts.");
        }
    }

    private void enforceAbsoluteLimit(BankAccount fromAccount, BigDecimal amount) {
        if (fromAccount.getBalance().subtract(amount).compareTo(fromAccount.getAbsoluteLimit()) < 0) {
            throw new IllegalArgumentException("Total amount cannot be less the absolute limit.");
        }
    }

    private void enforceWithinSingleTransferLimit(BankAccount fromAccount, BigDecimal amount) {
        if (fromAccount.getSingleTransferLimit().compareTo(amount) < 0) {
            throw new IllegalArgumentException("Transfer amount exceeds single transfer limit.");
        }
    }

    private void enforceNotSameAccountTransfer(BankAccount fromAccount, BankAccount toAccount) {
        if (fromAccount.getIban().equals(toAccount.getIban())) {
            throw new IllegalArgumentException("Transfer to the same account is not allowed.");
        }
    }

    private void enforcePositiveAmount(BigDecimal amount) {
        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0.");
        }
    }

    private void enforceNotDiffrentUserSavingsTransfer(BankAccount fromAccount, BankAccount toAccount) {
        if (fromAccount.getUser().getId() != toAccount.getUser().getId() && ( fromAccount.getBankAccountType() == MoneyMachine.models.enums.BankAccountType.SAVINGS || toAccount.getBankAccountType() == MoneyMachine.models.enums.BankAccountType.SAVINGS)) {
            throw new IllegalArgumentException("Transfer between different users' savings accounts is not allowed.");
        }
    }
}
