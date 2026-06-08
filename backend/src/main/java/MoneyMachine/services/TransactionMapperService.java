package MoneyMachine.services;


import java.util.List;

import org.springframework.stereotype.Service;

import MoneyMachine.mappers.TransactionMapper;
import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.Transaction;
import MoneyMachine.models.TransferTransaction;
import MoneyMachine.models.User;
import MoneyMachine.models.WithdrawTransaction;
import MoneyMachine.models.dtos.requests.TransferRequest;
import MoneyMachine.models.dtos.responses.ITransactionResponse;
import MoneyMachine.models.dtos.responses.TransferTransactionResponse;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.repositories.UserRepository;

@Service
public class TransactionMapperService {
    
    
    TransactionMapper mapper;
    UserRepository userRepository;
    BankAccountRepository bankAccountRepository;
    TransactionMapperService(TransactionMapper mapper, UserRepository userRepository,BankAccountRepository bankAccountRepository)
    {
        this.mapper = mapper;
        this.userRepository = userRepository;
        this.bankAccountRepository = bankAccountRepository;
       
    }
    public List<ITransactionResponse> getAllTransactions(List<Transaction> transactions) {
        return transactions
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public ITransactionResponse toResponse(Transaction t) {
        TransferTransactionResponse response = mapper.toResponse(t);

        switch (t) 
        {
            case TransferTransaction transfer -> 
            {
                response.setFromAccountIban(transfer.getFromBankAccount().getIban());
                response.setToAccountIban(transfer.getToBankAccount().getIban());
            }

            case WithdrawTransaction withdraw -> {
                response.setFromAccountIban(withdraw.getFromBankAccount().getIban());
            }

            case DepositTransaction deposit -> {
                response.setToAccountIban(deposit.getToBankAccount().getIban());
            }

            default -> throw new IllegalArgumentException(
                "Unknown type: " + t.getClass()
            );
        }

        return response;
    }

    public TransferTransaction toTransferEntity( TransferRequest transfer,User user) {
       
        TransferTransaction t= mapper.toTransferEntity(transfer);
        t.setInitiatingUser(user);
        return t;
    }
}
