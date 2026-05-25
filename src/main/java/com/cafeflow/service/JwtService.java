package com.cafeflow.config;

import com.cafeflow.model.User;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Service;
import java.security.Key;
import java.util.Date;

@Service
public class JwtService {

    // Geramos uma chave secreta segura para assinar os tokens
    private final Key key = Keys.secretKeyFor(SignatureAlgorithm.HS256);

    // O token expira em 1 dia (86400000 milissegundos)
    private final long expirationTime = 86400000;

    public String generateToken(User user) {
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getRole()) // Guarda o perfil (ADMIN ou USER) no token [1]
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expirationTime))
                .signWith(key)
                .compact();
    }
}
