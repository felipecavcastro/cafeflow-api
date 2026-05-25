package com.cafeflow.controller;

import com.cafeflow.model.User;
import com.cafeflow.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserController(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Endpoint público para qualquer nômade ou administrador se cadastrar
    @PostMapping("/register")
    public User register(@RequestBody User user) {
        // Criptografa a senha antes de mandar para o banco de dados SQLite
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);

        return userRepository.save(user);
    }
}
