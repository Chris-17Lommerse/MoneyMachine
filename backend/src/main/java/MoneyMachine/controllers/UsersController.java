package MoneyMachine.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import MoneyMachine.models.dtos.UserDTO;
import MoneyMachine.models.dtos.UserOverviewDTO;
import MoneyMachine.services.interfaces.UserService;

@RestController
@RequestMapping("/user")
public class UsersController {
    private final UserService userService;

    public UsersController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<UserOverviewDTO> getAllUsersWithoutAnAccount() 
    {
            List<UserDTO> users = userService.getAllUsersWithoutBankAccounts();
            UserOverviewDTO userOverviewDTO = new UserOverviewDTO();
            userOverviewDTO.setUsers(users);
            return ResponseEntity.ok(userOverviewDTO);
    }
}
