package MoneyMachine.mappers;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import java.util.ArrayList;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.responses.UserResponse;
import MoneyMachine.models.enums.Role;

public class UserMapperTest {
    
    private List<User> users;
    private User user;
    private UserResponse userResponse;
    private UserMapper userMapper;

    @BeforeEach
    void setUp() {
        users = new ArrayList<User>();
        userMapper = new UserMapper();

        userResponse = new UserResponse();
        user = new User();
        user.setId(Long.valueOf(1));
        user.setFirstName("employeeFirstName");
        user.setLastName("employeeLastName");
        user.setEmail("employee@employee.employee");
        user.setBsn("123456749");
        user.setPhoneNumber("+31 6 12 34 54 78");
        user.setRole(Role.EMPLOYEE);
        user.setIsApproved(false);
        user.setIsActive(false);
        userResponse.setUserId(1L);
        userResponse.setFirstName("employeeFirstName");
        userResponse.setLastName("employeeLastName");
        userResponse.setEmail("employee@employee.employee");
        userResponse.setBsn("123456749");
        userResponse.setPhoneNumber("+31 6 12 34 54 78");
        userResponse.setRole(Role.EMPLOYEE);
        userResponse.setApproved(false);
        userResponse.setActive(false);
    }

    @Test
    void toResponseShouldNotBeNull() {
        userResponse = userMapper.toResponse(user);
        assertEquals(user.getId(), userResponse.getUserId());
        assertEquals(user.getFirstName(), userResponse.getFirstName());
        assertEquals(user.getLastName(), userResponse.getLastName());
        assertEquals(user.getEmail(), userResponse.getEmail());
        assertEquals(user.getBsn(), userResponse.getBsn());
        assertEquals(user.getPhoneNumber(), userResponse.getPhoneNumber());
        assertEquals(user.getRole(), userResponse.getRole());
        assertEquals(user.getIsApproved(), userResponse.isApproved());
        assertEquals(user.getIsActive(), userResponse.isActive());
    }

    @Test
    void toEntityTest() {
        user = userMapper.toEntity(userResponse);
        assertEquals(userResponse.getUserId(), user.getId());
        assertEquals(userResponse.getFirstName(), user.getFirstName());
        assertEquals(userResponse.getLastName(), user.getLastName());
        assertEquals(userResponse.getEmail(), user.getEmail());
        assertEquals(userResponse.getBsn(), user.getBsn());
        assertEquals(userResponse.getPhoneNumber(), user.getPhoneNumber());
        assertEquals(userResponse.getRole(), user.getRole());
        assertEquals(userResponse.isApproved(), user.getIsApproved());
        assertEquals(userResponse.isActive(), user.getIsActive());
    }

    @Test
    void toResponseList() {
        users.add(user);
        List<UserResponse> userResponses = userMapper.toResponseList(users);
        assertEquals(userResponses.size(), 1);
        assertEquals(userResponses.getFirst().getUserId(), user.getId());
    }
}