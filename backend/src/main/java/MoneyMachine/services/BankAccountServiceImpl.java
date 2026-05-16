package MoneyMachine.services;

import MoneyMachine.mappers.BankAccountMapper;
import java.math.BigDecimal;

import org.springframework.stereotype.Service;

import MoneyMachine.models.enums.BankAccountType;
import MoneyMachine.models.enums.Role;
import MoneyMachine.repositories.BankAccountRepository;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.services.interfaces.BankAccountService;
import MoneyMachine.exception.NotAuthorizedException;
import MoneyMachine.exception.NotFoundException;
import MoneyMachine.factories.IbanGenerator;
import MoneyMachine.models.BankAccount;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.requests.BankAccountCreationRequest;
import MoneyMachine.models.dtos.responses.BankAccountResponse;
@Service
public class BankAccountServiceImpl implements BankAccountService {
    private BankAccountMapper bankAccountMapper;
    private BankAccountRepository bankAccountRepository;
    private UserRepository userRepository;
    private static final BigDecimal singleTransferLimit;
    private static final BigDecimal dailyTransferLimit; 

    public BankAccountServiceImpl(BankAccountRepository bankAccountRepository, UserRepository userRepository, BankAccountMapper bankAccountMapper)
    {
        this.bankAccountRepository = bankAccountRepository;
        this.userRepository = userRepository;
        this.bankAccountMapper = bankAccountMapper;
    }
    public BankAccountResponse createBankAccount(BankAccountCreationRequest bankAccountCreationRequest)
    {
       User user = userRepository.findById(userId);

       if(user == null)
       {
            throw new NotFoundException("User with user id");
       }

       if(user.getIsActive() == false)
       {
          throw new NotAuthorizedException("User is not active");
       }

       if(user.getRole() == Role.USER)
       {
            throw new NotAuthorizedException("User is not allowed to create account");
       }

       if(bankAccountType == bankAccountType.CHECKING)
       {
            // Code to be determined
       }

       if(bankAccountType == bankAccountType.SAVINGS)
       {
           // Code to be determined
       }
       
       BankAccount bankAccount = new BankAccount();

       bankAccountRepository.save(bankAccount);
       BankAccountResponse bankAccountRespnse = bankAccountMapper.toResponse(bankAccount);
       return bankAccountRespnse;
    }
}
