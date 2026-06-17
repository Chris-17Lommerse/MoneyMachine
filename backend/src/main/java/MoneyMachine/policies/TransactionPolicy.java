package MoneyMachine.policies;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

import MoneyMachine.exception.NotAuthorizedException;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.User;
import MoneyMachine.models.enums.Role;
import MoneyMachine.repositories.TransactionRepository;

@Component
public class TransactionPolicy {
    private TransactionRepository transactionRepository;
    public TransactionPolicy(TransactionRepository transactionRepository)
    {
       this.transactionRepository=transactionRepository;

    }

    public void enforceTransactionTransferPolicy(User user, BigDecimal amount, BankAccount fromBankAccount, BankAccount toBankAccount) {
        
        enforceTransactionPolicy(user, amount, fromBankAccount);
        enforceNotSameAccountTransfer(fromBankAccount, toBankAccount);
        enforceNotDifferentUserSavingsTransfer(fromBankAccount, toBankAccount);
        enforceWithinAbsoluteLimit(fromBankAccount, amount);
        enforceLimitsIfNessecary(fromBankAccount, toBankAccount, amount);    
    }

    public void enforceTransactionWithdrawPolicy(User user, BigDecimal amount, BankAccount fromBankAccount) {
        
        enforceTransactionPolicy(user, amount, fromBankAccount);
        enforceWithinAbsoluteLimit(fromBankAccount, amount);
        enforceWithinSingleTransferLimit(fromBankAccount, amount); 
    }
    public void enforceTransactionDepositPolicy(User user, BigDecimal amount, BankAccount fromBankAccount) {
        
        enforceTransactionPolicy(user, amount, fromBankAccount);
        enforceWithinSingleTransferLimit(fromBankAccount, amount);
    }

    public void enforceTransactionPolicy(User user, BigDecimal amount, BankAccount bankAccount) {
        
        if (user.getRole() == Role.USER) {
            enforceUserIsApproved(user);
            enforceUserIsActive(user);
            enforceUserOwnsBankAccountAccount(user, bankAccount);
        }
        enforcePositiveAmount(amount);
        enforceWithinSingleTransferLimit(bankAccount, amount);
    }
    private void enforceLimitsIfNessecary(BankAccount fromBankAccount, BankAccount toBankAccount, BigDecimal amount) {
        if (fromBankAccount.getUser().getId() != toBankAccount.getUser().getId()) 
        { 
            BigDecimal todayTransferredAmount = transactionRepository.findSpendAmountForIbanBetweentimes(fromBankAccount.getIban(), java.time.LocalDateTime.now().toLocalDate().atStartOfDay(), java.time.LocalDateTime.now().toLocalDate().atTime(23, 59, 59));
            if(todayTransferredAmount==null)
            {
                todayTransferredAmount=BigDecimal.ZERO;
            }
            enforceNotOverDailyLimit(amount, todayTransferredAmount, fromBankAccount.getDailyTransferLimit()); 
            enforceWithinSingleTransferLimit(fromBankAccount, amount); 
        } 

       
    }

    private void enforceUserOwnsBankAccountAccount(User user, BankAccount fromAccount) {

        if (fromAccount.getUser().getId() != user.getId()) {
            throw new NotAuthorizedException("Users can only transfer from their own accounts.");
        }
    }

    private void enforceUserIsApproved(User user) {

        if (user.getIsApproved() == false) {
            throw new NotAuthorizedException("Only approved users can make transactions.");
        }
    }

    private void enforceUserIsActive(User user) {

        if (user.getIsActive() == false) {
            throw new NotAuthorizedException("Only active users can make transactions.");
        }
    }

    private void enforceWithinAbsoluteLimit(BankAccount fromAccount, BigDecimal amount) {

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

    private void enforceNotDifferentUserSavingsTransfer(BankAccount fromAccount, BankAccount toAccount) {
        
        if (fromAccount.getUser().getId() != toAccount.getUser().getId() && ( fromAccount.getBankAccountType() == MoneyMachine.models.enums.BankAccountType.SAVINGS || toAccount.getBankAccountType() == MoneyMachine.models.enums.BankAccountType.SAVINGS)) {
            throw new IllegalArgumentException("Transfer between different users' savings accounts is not allowed.");
        }
    }
    private void enforceNotOverDailyLimit( BigDecimal amount,BigDecimal todayTransferredAmount, BigDecimal dailyTransferLimit) {
        if (todayTransferredAmount.add(amount).compareTo(dailyTransferLimit) > 0) {
            throw new IllegalArgumentException("Transfer amount exceeds daily transfer limit.");
        }
    }
}
