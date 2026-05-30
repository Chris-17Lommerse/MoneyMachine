package MoneyMachine.mappers;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.List;
import MoneyMachine.models.User;
import MoneyMachine.models.enums.Role;
public class UserMapperTest {
    private List<User> users;
    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(Long.valueOf(1));
        user.setFirstName("employeeFirstName");
        user.setLastName("employeeLastName");
        user.setEmail("employee@employee.employee");
        user.setBsn("123456749");
        user.setPhoneNumber("+31 6 12 34 54 78");
        user.setRole(Role.EMPLOYEE);
        user.setPassword("password");
        user.setIsApproved(false);
        user.setIsActive(false);
    }

    @Test
    void toResponseShouldNotBeNull() {
        UserMapper userMapper = new UserMapper();
        userMapper.toResponse(user);
        assertNotNull(userMapper);
        assertNotEquals(user.getBsn(), userMapper.toResponse(user).getBsn());
        assertNotNull(user.getFirstName(), userMapper.toResponse(user).getFirstName());
    }

    @Test
    void toSummaryResponseShuldNotBeNull()
    {
        UserMapper userMapper = new UserMapper();
        userMapper.toSummaryResponse(user);
        assertNotNull(user.getFirstName(), userMapper.toSummaryResponse(user).getFirstName());
    }

    @Test
    void toResponseList()
    {
        UserMapper userMapper = new UserMapper();
        userMapper.toResponseList(users);
        assertNotEquals(userMapper.toResponseList(users).size(), 0);
    }
}