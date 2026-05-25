package MoneyMachine.services;


import java.util.List;

import org.springframework.stereotype.Service;

import MoneyMachine.mappers.TransactionMapper;
import MoneyMachine.models.DepositTransaction;
import MoneyMachine.models.Transaction;
import MoneyMachine.models.TransferTransaction;
import MoneyMachine.models.User;
import MoneyMachine.models.WithdrawTransaction;
import MoneyMachine.models.dtos.requests.DepositRequest;
import MoneyMachine.models.dtos.requests.TransferRequest;
import MoneyMachine.models.dtos.requests.WithdrawRequest;
import MoneyMachine.models.dtos.responses.TransactionResponse;
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
    public List<TransactionResponse> getAllTransactions( List<Transaction> transactions) {
        return transactions
                .stream()
                .map(this::toResponse)
                .toList();
    }

    public TransactionResponse toResponse(Transaction t) {
        TransactionResponse response = mapper.toResponse(t);

       switch (t) 
        {

            case TransferTransaction transfer -> 
            {
                response.setFromAccount(transfer.getFromBankAccount().getIban());
                response.setToAccount(transfer.getToBankAccount().getIban());
                response.setType("TRANSFER");
            }

            case WithdrawTransaction withdraw -> {
                response.setFromAccount(withdraw.getFromBankAccount().getIban());
                response.setType("WITHDRAW");
            }

            case DepositTransaction deposit -> {
                response.setToAccount(deposit.getToBankAccount().getIban());
                response.setType("DEPOSIT");
            }

            default -> throw new IllegalArgumentException(
                "Unknown type: " + t.getClass()
            );
        }

        return response;
    }
    public TransferTransaction toTransferEntity( TransferRequest transfer) {
       
        TransferTransaction t= mapper.toTransferEntity(transfer);
        User user =userRepository.findById(transfer.getInitiatedBy()).orElseThrow(() -> new RuntimeException("User not found")) ;
        t.setInitiatingUser(user);
        t.setActive(true);
        return t;

    }
    public WithdrawTransaction toWithdrawEntity( WithdrawRequest withdraw) {
       
        WithdrawTransaction t= mapper.toWithdrawEntity(withdraw);
        User user =userRepository.findById(withdraw.getInitiatedBy()).orElseThrow(() -> new RuntimeException("User not found")) ;
        t.setInitiatingUser(user);
        t.setActive(true);
        return t;
    }
    public DepositTransaction toDepositEntity( DepositRequest deposit) {
       
        DepositTransaction t= mapper.toDepositEntity(deposit);
        User user =userRepository.findById(deposit.getInitiatedBy()).orElseThrow(() -> new RuntimeException("User not found")) ;
        t.setInitiatingUser(user);
        t.setActive(true);
        return t;
    }
    

}
