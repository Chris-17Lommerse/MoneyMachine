package MoneyMachine.controllers;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import MoneyMachine.dto.UserDTO;
import MoneyMachine.mappers.UserMapper;
import MoneyMachine.models.User;
import MoneyMachine.services.UserServiceJpa;

@RestController
@RequestMapping("/user")
public class UsersController {
    private final UserServiceJpa userService;
    private UserMapper userMapper;

    public UsersController(UserServiceJpa userService, UserMapper userMapper) {
        this.userService = userService;
        this.userMapper = userMapper;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity<UserDTO> getAllUsersWithoutAnAccount() 
    {
            List<User> users = userService.getAllUsersWithoutAnAccount();
            return ResponseEntity.ok(userMapper.toDTO(users))
    }
}
