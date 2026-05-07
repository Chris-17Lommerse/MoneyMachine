package MoneyMachine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestBody;

import MoneyMachine.models.User;
import MoneyMachine.models.dtos.LoginDTO;
import MoneyMachine.models.dtos.UserDTO;
import MoneyMachine.models.enums.LoginType;
import MoneyMachine.models.requestBodies.LoginRequestBody;
import MoneyMachine.services.Interfaces.AuthenticationService;

@RestController
@RequestMapping("/atm")
public class ATMAuthenticationController {
    
    private AuthenticationService authenticationService;

    public ATMAuthenticationController(AuthenticationService authenticationService){
        this.authenticationService = authenticationService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequestBody loginRequestBody) {
        
        User user = authenticationService.getUserByEmailAndPassword(loginRequestBody.getEmail(), loginRequestBody.getPassword());

        if (user != null){
            LoginDTO loginDto = new LoginDTO("test jwt", LoginType.atm);

            return ResponseEntity.status(201).body(loginDto);
        }

        return ResponseEntity.status(400).body("nah");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        return ResponseEntity.status(204).body(null);
    }

    @GetMapping("/user-test")
    public ResponseEntity<UserDTO> userTest() {
        return ResponseEntity.status(204).body(null);
    }
}
