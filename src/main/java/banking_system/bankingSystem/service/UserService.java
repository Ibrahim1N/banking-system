package banking_system.bankingSystem.service;

import banking_system.bankingSystem.customException.InvalidCredentialsException;
import banking_system.bankingSystem.customException.UserNotFoundException;
import banking_system.bankingSystem.model.User;
import banking_system.bankingSystem.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public void registerUser(User user) throws InvalidCredentialsException {
        User existingUser = userRepository.findByEmail(user.getEmail());
        if(existingUser != null){
            throw new InvalidCredentialsException("User with this email already exists");
        }
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        user.setPassword(encoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public User loginUser(String email, String password) throws UserNotFoundException, InvalidCredentialsException {
        User existingUser = userRepository.findByEmail(email);
        if(existingUser == null){
            throw  new UserNotFoundException("User not found");
        }

        if(!encoder.matches(password, existingUser.getPassword())){
            throw new InvalidCredentialsException("Incorrect password");
        }
        return existingUser;
    }

    public User getUserById(Long id) throws UserNotFoundException {
        Optional<User> existingUser = userRepository.findById(id);
        if(existingUser.isEmpty()){
            throw new UserNotFoundException("User not found");
        }
        return existingUser.get();
    }

}
