package dev.pabloabad.adventuretime.users;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "${api-endpoint}")
public class AuthController {
    
    private final UserRepository userRepository;

    public AuthController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping(path = "/login")
    public ResponseEntity<Map<String, String>> login() {

        SecurityContext contextHolder = SecurityContextHolder.getContext();
        Authentication auth = contextHolder.getAuthentication();

        String username = auth.getName();

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found: " + username));

        Map<String,String> json = new HashMap<>();
        json.put("message", "Logged");
        json.put("id", String.valueOf(user.getId()));
        json.put("username", auth.getName());
        json.put("roles", auth.getAuthorities().iterator().next().toString());

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(json);
    }
}
