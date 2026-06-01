package MoneyMachine.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
import MoneyMachine.exception.InvalidCredentialsException;

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
    
    private User user;
    private UserSummaryResponse userSummary;
    private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @BeforeEach
    void setUp() {
        user = new User();
        user.setFirstName("userFirstName");
        user.setLastName("userLastName");
        user.setEmail("user@user.user");
        user.setBsn("123456789");
        user.setPhoneNumber("+31 6 12 34 56 78");
        user.setRole(Role.USER);
        user.setPassword(passwordEncoder.encode("password"));
        user.setIsActive(true);
        user.setIsApproved(true);

        userSummary = new UserSummaryResponse();
        userSummary.setId(user.getId());
        userSummary.setFirstName(user.getFirstName());
        userSummary.setLastName(user.getLastName());
        userSummary.setEmail(user.getEmail());
    }

    @Test
    void login_whenLoginAsUser_getAuthenticationResponse() {

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(passwordEncoderMock.matches("password", user.getPassword())).thenReturn(true);
        when(jwtUtil.generateAuthTokenFromUser(user, LoginType.ATM)).thenReturn("Example JWT");
        when(jwtUtil.getAuthTokenExpirationTime()).thenReturn(new Date());
        when(userMapper.toSummaryResponse(user)).thenReturn(userSummary);
        
        LoginResponse loginResponse = authenticationService.login(user.getEmail(), "password", LoginType.ATM);

        assertEquals(loginResponse.getUserSummaryResponse().getEmail(), user.getEmail());

        verify(userRepository).findByEmail(user.getEmail());
        verify(jwtUtil).generateAuthTokenFromUser(user, LoginType.ATM);
    }

    @Test
    void login_whenInvalidLogin_throwInvalidCredentialsException() {

        when(userRepository.findByEmail(user.getEmail())).thenReturn(user);
        when(passwordEncoderMock.matches("invalidPassword", user.getPassword())).thenReturn(false);
        
        assertThrows(InvalidCredentialsException.class, () ->
            authenticationService.login(user.getEmail(), "invalidPassword", LoginType.ATM)
        );

        verify(userRepository).findByEmail(user.getEmail());
        verify(passwordEncoderMock).matches("invalidPassword", user.getPassword());
    }
}
