package banking_system.bankingSystem.controller;


import banking_system.bankingSystem.customException.InvalidCredentialsException;
import banking_system.bankingSystem.customException.UserNotFoundException;
import banking_system.bankingSystem.dto.JwtResponse;
import banking_system.bankingSystem.dto.JwtUtil;
import banking_system.bankingSystem.model.User;
import banking_system.bankingSystem.repository.UserRepository;
import banking_system.bankingSystem.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/users")
public class UserController {

    private final JwtUtil jwtUtil;

    private final UserRepository userRepository;
    private final UserService userService;

    public UserController(JwtUtil jwtUtil, UserRepository userRepository, UserService userService) {
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.userService = userService;
    }

    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody User user) throws InvalidCredentialsException {
        try {
            userService.registerUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Email already in use");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> loginUser(@RequestParam String email, @RequestParam String password) throws InvalidCredentialsException, UserNotFoundException {
        try {
            User user = userService.loginUser(email, password);
            String token = jwtUtil.generateToken(user);
            return ResponseEntity.ok().body(new JwtResponse(token));
        } catch (UserNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found");
        } catch (InvalidCredentialsException e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials");
        }
    }


}
