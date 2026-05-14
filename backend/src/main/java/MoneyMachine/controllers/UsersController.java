package MoneyMachine.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import MoneyMachine.models.dtos.ErrorDTO;
import MoneyMachine.models.dtos.UserDTO;
import MoneyMachine.models.dtos.UserOverviewDTO;
import MoneyMachine.models.enums.ErrorType;
import MoneyMachine.services.interfaces.UserService;

@RestController
@RequestMapping("/user")
public class UsersController extends BaseController {
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<?> getAllUsersWithoutAnAccount() {
        try {
            List<UserDTO> users = userService.getAllUsersWithoutBankAccounts();
            UserOverviewDTO userOverviewDTO = new UserOverviewDTO();
            userOverviewDTO.setUsers(users);
            return ResponseEntity.ok(userOverviewDTO);
        } catch (Unauthorized exUnauthorized) {
            ErrorDTO errorDTO = new ErrorDTO(401, MoneyMachine.models.enums.ErrorType.UNAUTHORIZED,
                    "Unauthorized - Authentication required", exUnauthorized.getMessage());
            return ResponseEntity.status(401).body(errorDTO);
        } catch (InternalServerError exInternalServerError) {
            ErrorDTO errorDTO = new ErrorDTO(500, ErrorType.BAD_REQUEST,
                    "Internal Server Error - An unexpected error occurred", exInternalServerError.getMessage());
            return ResponseEntity.status(500).body(errorDTO);
        }
    }
}
