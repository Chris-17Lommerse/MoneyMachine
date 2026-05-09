package MoneyMachine.mappers;

import org.springframework.stereotype.Component;

import MoneyMachine.models.*;

import java.util.ArrayList;
import java.util.List;
import MoneyMachine.models.dtos.UserDTO;

@Component
public class UserMapper {
    public UserDTO toDTO(User user)
    {
        UserDTO dto = new UserDTO();
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

    public User toEntity(UserDTO dto) {
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

    public List<UserDTO> toDTOList(List<User> userList) {
        List<UserDTO> userDTOs = new ArrayList<UserDTO>();
        for(User user : userList)
        {
            userDTOs.add(toDTO(user));
        }
        return userDTOs;
    }
}
