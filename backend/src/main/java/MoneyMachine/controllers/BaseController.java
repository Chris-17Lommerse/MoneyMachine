package MoneyMachine.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import lombok.NoArgsConstructor;

@Controller
@CrossOrigin(origins = "*", exposedHeaders = {"x-atm-auth-error"})
@NoArgsConstructor
public class BaseController {}
