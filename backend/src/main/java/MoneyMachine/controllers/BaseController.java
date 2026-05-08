package MoneyMachine.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;

import MoneyMachine.models.User;

@Controller
@CrossOrigin(origins = "*")
public class BaseController {
    
    User currentLoggedInAtmUser = null;
}
