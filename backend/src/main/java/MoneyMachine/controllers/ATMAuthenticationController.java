package MoneyMachine.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import MoneyMachine.models.dtos.LoginDTO;
import MoneyMachine.models.dtos.UserDTO;
import MoneyMachine.models.enums.LoginType;

@RestController
@RequestMapping("/atm")
public class ATMAuthenticationController {
    
    @PostMapping("/login")
    public ResponseEntity<LoginDTO> login() {

        LoginDTO loginDto = new LoginDTO("hello", LoginType.atm);

        return ResponseEntity.status(201).body(loginDto);
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
