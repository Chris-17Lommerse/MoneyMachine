package MoneyMachine.controllers;

import java.util.List;

import org.apache.tomcat.util.http.parser.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.client.HttpClientErrorException.Unauthorized;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import MoneyMachine.models.User;
import MoneyMachine.services.UserService;


@Controller
@RequestMapping("/user")
public class UsersController {
    private final UserService userService;

    public UsersController(UserService userService)
    {
        this.userService = userService;
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public ResponseEntity getAllUsersWithoutAnAccount() 
    {
        try
        {
            List<User> users = userService.getAllUsersWithoutAnAccount();
            return ResponseEntity.status(200).body(users);
        } catch (InternalServerError ex)
        {
            return ResponseEntity.status(500).body(ex);
        }
        catch (Unauthorized ex)
        {
            return ResponseEntity.status(401).body(ex);
        }
    }

    
}
