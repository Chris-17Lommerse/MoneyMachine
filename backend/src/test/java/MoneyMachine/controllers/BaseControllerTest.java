package MoneyMachine.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.transaction.annotation.Transactional;

import tools.jackson.databind.ObjectMapper;

import MoneyMachine.models.User;
import MoneyMachine.models.enums.LoginType;
import MoneyMachine.models.enums.Role;
import MoneyMachine.util.JwtUtil;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class BaseControllerTest {
    
    @Autowired
    public MockMvc mockMvc;
    @Autowired
    public ObjectMapper objectMapper;

    @Autowired
    private JwtUtil jwtUtil;

    public User user;
    public User employee;

    public String atmUserAuthToken;
    public String atmEmployeeAuthToken;
    public String websiteUserAuthToken;
    public String websiteEmployeeAuthToken;

    void setUpMockAuth() {
        
        user = new User();
        user.setId(1L);
        user.setFirstName("userFirstName");
        user.setLastName("userLastName");
        user.setEmail("user@user.user");
        user.setRole(Role.USER);

        employee = new User();
        employee.setId(2L);
        employee.setFirstName("employeeFirstName");
        employee.setLastName("employeeLastName");
        employee.setEmail("employee@employee.employee");
        employee.setRole(Role.EMPLOYEE);
    
        atmUserAuthToken = jwtUtil.generateAuthTokenFromUser(user, LoginType.ATM);
        atmEmployeeAuthToken = jwtUtil.generateAuthTokenFromUser(employee, LoginType.ATM);
        websiteUserAuthToken = jwtUtil.generateAuthTokenFromUser(user, LoginType.WEBSITE);
        websiteEmployeeAuthToken = jwtUtil.generateAuthTokenFromUser(employee, LoginType.WEBSITE);
    }
}
