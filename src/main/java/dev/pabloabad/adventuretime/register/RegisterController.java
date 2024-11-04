package dev.pabloabad.adventuretime.register;

import java.util.HashMap;
import java.util.Map;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dev.pabloabad.adventuretime.dtos.RegisterDto;
import dev.pabloabad.adventuretime.users.User;

@RestController
@RequestMapping(path = "${api-endpoint}/register")
public class RegisterController {
    
     private final RegisterService service;

    public RegisterController(RegisterService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Map<String, String>> register(@Valid @RequestBody RegisterDto newUser) {
        User user = service.save(newUser);

        Map<String, String> json = new HashMap<>();
        json.put("message", "Register successful");
        json.put("username", user.getUsername());

        return ResponseEntity.status(HttpStatus.CREATED).body(json);
    }
}
