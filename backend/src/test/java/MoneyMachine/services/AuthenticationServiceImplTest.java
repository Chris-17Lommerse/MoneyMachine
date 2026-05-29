package MoneyMachine.services;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.extension.ExtendWith;

import MoneyMachine.mappers.UserMapper;
import MoneyMachine.models.User;
import MoneyMachine.models.dtos.responses.LoginResponse;
import MoneyMachine.models.dtos.responses.UserSummaryResponse;
import MoneyMachine.models.enums.LoginType;
import MoneyMachine.models.enums.Role;
import MoneyMachine.repositories.UserRepository;
import MoneyMachine.util.JwtUtil;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Date;

@ExtendWith(MockitoExtension.class)
public class AuthenticationServiceImplTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private PasswordEncoder passwordEncoderMock;
    @Mock
    private JwtUtil jwtUtil;
    @Mock
    private UserMapper userMapper;
    @InjectMocks
    private AuthenticationServiceImpl authenticationService;
    
    private User employee;
    private UserSummaryResponse employeeSummary;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        employee = new User();
        employee.setId(2L);
        employee.setFirstName("employeeFirstName");
        employee.setLastName("employeeLastName");
        employee.setEmail("employee@employee.employee");
        employee.setBsn("123456749");
        employee.setPhoneNumber("+31 6 12 34 54 78");
        employee.setRole(Role.EMPLOYEE);
        employee.setPassword(passwordEncoder.encode("password"));
        employee.setIsActive(false);
        employee.setIsApproved(false);

        employeeSummary = new UserSummaryResponse();
        employeeSummary.setId(employee.getId());
        employeeSummary.setFirstName(employee.getFirstName());
        employeeSummary.setLastName(employee.getLastName());
        employeeSummary.setEmail(employee.getEmail());
    }

    @Test
    void login_whenLoginAsEmployee_getAuthenticationResponse() {

        when(userRepository.findByEmail(employee.getEmail())).thenReturn(employee);
        when(passwordEncoderMock.matches("password", employee.getPassword())).thenReturn(true);
        when(jwtUtil.generateAuthTokenFromUser(employee, LoginType.ATM)).thenReturn("Example JWT");
        when(jwtUtil.getAuthTokenExpirationTime()).thenReturn(new Date());
        when(userMapper.toSummaryResponse(employee)).thenReturn(employeeSummary);
        
        LoginResponse loginResponse = authenticationService.login(employee.getEmail(), "password", LoginType.ATM);

        assertEquals(loginResponse.getUserSummaryResponse().getEmail(), employee.getEmail());

        verify(userRepository).findByEmail(employee.getEmail());
        verify(jwtUtil).generateAuthTokenFromUser(employee, LoginType.ATM);
    }
}
