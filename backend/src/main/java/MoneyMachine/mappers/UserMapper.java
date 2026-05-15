package MoneyMachine.mappers;

import org.springframework.stereotype.Component;

import MoneyMachine.models.*;

import java.util.ArrayList;
import java.util.List;
import MoneyMachine.models.dtos.UserResponse;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {

        UserResponse dto = new UserResponse();
        dto.setUserId(user.getId());
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setBsn(user.getBsn());
        dto.setPhoneNumber(user.getPhoneNumber());
        dto.setRole(user.getRole());
        dto.setActive(user.getIsActive());
        dto.setApproved(user.getIsApproved());

        return dto;
    }

    public User toEntity(UserResponse dto) {

        User user = new User();
        user.setId(dto.getUserId());
        user.setFirstName(dto.getFirstName());
        user.setLastName(dto.getLastName());
        user.setEmail(dto.getEmail());
        user.setBsn(dto.getBsn());
        user.setPhoneNumber(dto.getPhoneNumber());
        user.setRole(dto.getRole());
        user.setIsActive(dto.isActive());
        user.setIsApproved(dto.isApproved());

        return user;
    }

    public List<UserResponse> toDTOList(List<User> userList) {

        List<UserResponse> userDTOs = new ArrayList<UserResponse>();

        for(User user : userList){
            userDTOs.add(toResponse(user));
        }

        return userDTOs;
    }
}
