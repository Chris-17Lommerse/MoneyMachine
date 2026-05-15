package MoneyMachine.mappers;

import org.springframework.stereotype.Component;

import MoneyMachine.models.*;
import MoneyMachine.models.dtos.responses.UserResponse;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {

        UserResponse response = new UserResponse();
        response.setUserId(user.getId());
        response.setFirstName(user.getFirstName());
        response.setLastName(user.getLastName());
        response.setEmail(user.getEmail());
        response.setBsn(user.getBsn());
        response.setPhoneNumber(user.getPhoneNumber());
        response.setRole(user.getRole());
        response.setActive(user.getIsActive());
        response.setApproved(user.getIsApproved());

        return response;
    }

    public User toEntity(UserResponse response) {

        User user = new User();
        user.setId(response.getUserId());
        user.setFirstName(response.getFirstName());
        user.setLastName(response.getLastName());
        user.setEmail(response.getEmail());
        user.setBsn(response.getBsn());
        user.setPhoneNumber(response.getPhoneNumber());
        user.setRole(response.getRole());
        user.setIsActive(response.isActive());
        user.setIsApproved(response.isApproved());

        return user;
    }

    public List<UserResponse> toDTOList(List<User> userList) {

        List<UserResponse> userResponses = new ArrayList<UserResponse>();

        for(User user : userList){
            userResponses.add(toResponse(user));
        }

        return userResponses;
    }
}
