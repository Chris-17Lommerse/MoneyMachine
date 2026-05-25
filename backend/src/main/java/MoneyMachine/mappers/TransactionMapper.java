package MoneyMachine.mappers;


import MoneyMachine.models.dtos.responses.TransactionResponse;
import MoneyMachine.models.dtos.requests.DepositRequest;
import MoneyMachine.models.dtos.requests.TransferRequest;
import MoneyMachine.models.dtos.requests.WithdrawRequest;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import MoneyMachine.models.*;

@Mapper(componentModel = "spring")
public interface TransactionMapper {

    @Mapping(source = "dateTime", target = "time")
    @Mapping(source = "initiatingUser.id", target = "initiatedBy")
    @Mapping(target = "toAccount", ignore = true)
    @Mapping(target = "fromAccount", ignore = true)
    @Mapping(target = "type", ignore = true)
    TransactionResponse toResponse(Transaction t);

    @Mapping(target = "fromBankAccount", ignore = true)
    @Mapping(target = "toBankAccount", ignore = true)
    @Mapping(target = "dateTime", ignore = true)
    @Mapping(target = "initiatingUser", ignore = true)
    @Mapping(target = "active", ignore = true)
    
    TransferTransaction toTransferEntity(TransferRequest r);

    @Mapping(target = "fromBankAccount", ignore = true)
    @Mapping(target = "dateTime", ignore = true)
    @Mapping(target = "initiatingUser", ignore = true)
    @Mapping(target = "active", ignore = true)
    WithdrawTransaction toWithdrawEntity(WithdrawRequest r);

    @Mapping(target = "toBankAccount", ignore = true)
    @Mapping(target = "dateTime", ignore = true)
    @Mapping(target = "initiatingUser", ignore = true)
    @Mapping(target = "active", ignore = true)

    DepositTransaction toDepositEntity(DepositRequest r);
}
