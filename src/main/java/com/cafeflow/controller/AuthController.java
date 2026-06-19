package com.cafeflow.controller;

import com.cafeflow.dto.LoginRequest;
import com.cafeflow.model.User;
import com.cafeflow.repository.UserRepository;

import com.cafeflow.service.JwtService;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtService jwtService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public String login(@RequestBody LoginRequest request) {
        // 1. Busca o usuário pelo e-mail
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new RuntimeException("Usuário ou senha incorretos!"));

        // 2. Compara a senha digitada com a criptografada do banco
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new RuntimeException("Usuário ou senha incorretos!");
        }

        // 3. Se deu tudo certo, gera e devolve o Token JWT
        return jwtService.generateToken(user);
    }
}
