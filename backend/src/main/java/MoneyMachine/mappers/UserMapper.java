package MoneyMachine.mappers;

import org.springframework.stereotype.Component;

import MoneyMachine.models.*;
import MoneyMachine.models.dtos.responses.UserResponse;
import MoneyMachine.models.dtos.responses.UserSummaryResponse;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {

        UserResponse userResponse = new UserResponse();
        userResponse.setUserId(user.getId());
        userResponse.setFirstName(user.getFirstName());
        userResponse.setLastName(user.getLastName());
        userResponse.setEmail(user.getEmail());
        userResponse.setBsn(user.getBsn());
        userResponse.setPhoneNumber(user.getPhoneNumber());
        userResponse.setRole(user.getRole());
        userResponse.setActive(user.getIsActive());
        userResponse.setApproved(user.getIsApproved());

        return userResponse;
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

    public UserSummaryResponse toSummaryResponse(User user) {

        UserSummaryResponse userSummaryResponse = new UserSummaryResponse();
        userSummaryResponse.setId(user.getId());
        userSummaryResponse.setFirstName(user.getFirstName());
        userSummaryResponse.setLastName(user.getLastName());
        userSummaryResponse.setEmail(user.getEmail());

        return userSummaryResponse;
    }

    public List<UserResponse> toResponseList(List<User> userList) {

        List<UserResponse> userResponses = new ArrayList<UserResponse>();

        for(User user : userList){
            userResponses.add(toResponse(user));
        }

        return userResponses;
    }
}
